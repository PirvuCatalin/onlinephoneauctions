package com.onlinephoneauctions.service;

import com.onlinephoneauctions.dbconnect.ConnectionUtil;
import com.onlinephoneauctions.dto.AuctionInfoDTO;
import com.onlinephoneauctions.dto.AuctionWithBidsDTO;
import com.onlinephoneauctions.dto.AvailablePhonesDTO;
import com.onlinephoneauctions.dto.BidDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class AuctionService {

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private UserService userService;

    /**
     * @return {@link List<AuctionInfoDTO>} containing the currently active auctions
     */
    public List<AuctionInfoDTO> retrieveActiveAuctions() {
        List<AuctionInfoDTO> activeAuctions = new ArrayList<>();
        List<HashMap<Integer, String>> activeAuctionsMap = ConnectionUtil.getMultipleColumns(
                "SELECT ai.id, " +
                        "phone_names.phone_names, " +
                        "ai.starting_price, " +
                        "ai.target_price, " +
                        "ai.current_price_bidded, " +
                        "ai.datetime_start, " +
                        "ai.datetime_end," +
                        "ai.seller_id, " +
                        "u.name, " +
                        "ai.additional_info, " +
                        "ai.is_successfully_done " +
                        "FROM auction_info ai " +
                        "INNER JOIN users u " +
                        "ON u.id = ai.seller_id " +
                        "INNER JOIN (" +
                        "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                        "FROM phones p, phones_in_auction pa " +
                        "WHERE p.id = pa.phone_id " +
                        "GROUP BY pa.auction_info_id" +
                        ") phone_names " +
                        "ON phone_names.auction_info_id = ai.id " +
                        "WHERE ai.DATETIME_END >= CURRENT_TIMESTAMP " +
                        "AND ai.is_Validated = true",
                11);
        return getAuctionInfoDTOS(activeAuctions, activeAuctionsMap);
    }

    /**
     * @return {@link AuctionInfoDTO} containing the information of the auction with the id specified by parameter {@param id}
     */
    public AuctionInfoDTO retrieveAuctionInfo(String id) {
        List<HashMap<Integer, String>> list = ConnectionUtil.getMultipleColumns(
                "SELECT ai.id, " +
                        "phone_names.phone_names, " +
                        "ai.starting_price, " +
                        "ai.target_price, " +
                        "ai.current_price_bidded, " +
                        "ai.datetime_start, " +
                        "ai.datetime_end," +
                        "ai.seller_id, " +
                        "u.name, " +
                        "ai.additional_info, " +
                        "ai.is_successfully_done " +
                        "FROM auction_info ai " +
                        "INNER JOIN users u " +
                        "ON u.id = ai.seller_id " +
                        "INNER JOIN (" +
                        "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                        "FROM phones p, phones_in_auction pa " +
                        "WHERE p.id = pa.phone_id " +
                        "GROUP BY pa.auction_info_id" +
                        ") phone_names " +
                        "ON phone_names.auction_info_id = ai.id " +
                        "WHERE    ai.id = '" + id + "'",
                11);
        AuctionInfoDTO auctionInfoDTO = new AuctionInfoDTO();
        HashMap<Integer, String> map = list.get(0);

        if (map != null && !map.isEmpty()) {
            auctionInfoDTO.setId(map.get(1));
            auctionInfoDTO.setTitle(map.get(2));
            auctionInfoDTO.setStarting_price(Double.parseDouble(map.get(3)));
            auctionInfoDTO.setTarget_price(Double.parseDouble(map.get(4)));
            auctionInfoDTO.setCurrent_price_bidded(Double.parseDouble(map.get(5)));
            auctionInfoDTO.setDatetime_start(map.get(6));
            auctionInfoDTO.setDatetime_end(map.get(7));
            auctionInfoDTO.setSeller_id(map.get(8));
            auctionInfoDTO.setSeller_name(map.get(9));
            auctionInfoDTO.setAdditional_info(map.get(10));
            auctionInfoDTO.setIs_successfully_done(Boolean.parseBoolean(map.get(11)));
        } else {
            auctionInfoDTO = null;
        }

        return auctionInfoDTO;
    }

    /**
     * This method retrieved the auctions, completely with info about the winner / highest bidder.
     * @return {@link List<BidDTO>} containing the bid of the auction specified by {@param id}
     */
    public List<BidDTO> retrieveBidsForAuction(String id) {
        List<BidDTO> listToReturn = new ArrayList<>();
        List<HashMap<Integer, String>> list = ConnectionUtil.getMultipleColumns(
                "select u.name, b.datetime_bidded, b.price_bidded " +
                        "from bids b, users u " +
                        "where b.auction_info_id = '" + id + "' " +
                        "and u.id = b.buyer_id " +
                        "order by datetime_bidded desc;",
                3);
        list.forEach(row -> {
            BidDTO bidDTO = new BidDTO();
            bidDTO.setBuyer_name(row.get(1));
            bidDTO.setDatetime_bidded(row.get(2));
            bidDTO.setPrice_bidded(Double.parseDouble(row.get(3)));
            listToReturn.add(bidDTO);
        });
        if (listToReturn.isEmpty()) {
            return null;
        }
        List<HashMap<Integer, String>> auctionInfoList = ConnectionUtil.getMultipleColumns(
                "select is_successfully_done " +
                        "from auction_info " +
                        "where id = '" + id + "' ",
                1);
        listToReturn.get(0).setHighest(true);
        if (auctionInfoList.get(0) != null && auctionInfoList.get(0).get(1).equals("TRUE")) {
            listToReturn.get(0).setWinner(true);
        }
        return listToReturn;
    }

    /**
     * @return {@link List<AuctionWithBidsDTO>} containing only the bids of the current logged in user specified by {@param userId},
     * also with information regarding the status (winner / highest bidder) and if the auction is ended and the current user is the winner,
     * it also gives the option to review the auction.
     */
    public List<AuctionWithBidsDTO> retrieveMyBidsByAuction(String userId) {
        List<AuctionWithBidsDTO> auctionWithBids = new ArrayList<>();
        List<BidDTO> bids = new ArrayList<>();
        List<HashMap<Integer, String>> list = ConnectionUtil.getMultipleColumns(
                "select a.id, phone_names.phone_names, a.datetime_end, a.current_price_bidded, b.datetime_bidded, b.price_bidded, seller.name, a.is_successfully_done " +
                        "from bids b, auction_info a, users u, users seller " +
                        "INNER JOIN (" +
                        "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                        "FROM phones p, phones_in_auction pa " +
                        "WHERE p.id = pa.phone_id " +
                        "GROUP BY pa.auction_info_id" +
                        ") phone_names " +
                        "ON phone_names.auction_info_id = a.id " +
                        "where u.id = '" + userId + "' " +
                        "and u.id = b.buyer_id " +
                        "and seller.id = a.seller_id " +
                        "and a.id = b.auction_info_id " +
                        "order by a.datetime_end desc, b.datetime_bidded desc;",
                8);
        String lastAuctionInfoId;
        if (!list.isEmpty() && list.get(0) != null) {
            lastAuctionInfoId = list.get(0).get(1);
        } else {
            return null;
        }

        AuctionWithBidsDTO auction = new AuctionWithBidsDTO();
        for (HashMap<Integer, String> row : list) {
            if (!lastAuctionInfoId.equals(row.get(1))) {
                auction.setBids(bids);
                auctionWithBids.add(auction);
                bids = new ArrayList<>();
                auction = new AuctionWithBidsDTO();
                lastAuctionInfoId = row.get(1);
            }
            auction.setAuction_title(row.get(2));
            auction.setAuction_datetime_end(row.get(3));
            auction.setAuction_current_price_bidded(Double.parseDouble(row.get(4)));
            auction.setAuction_seller(row.get(7));
            auction.setIs_successfully_done(Boolean.parseBoolean(row.get(8)));
            auction.setAuction_info_id(row.get(1));

            BidDTO bid = new BidDTO();
            bid.setDatetime_bidded(row.get(5));
            bid.setPrice_bidded(Double.parseDouble(row.get(6)));
            bids.add(bid);
        }
        auction.setBids(bids);
        auctionWithBids.add(auction);

        auctionWithBids.forEach(auc -> {
            if (auc.getBids().get(0).getPrice_bidded() == auc.getAuction_current_price_bidded()) {
                if (auc.isIs_successfully_done()) {
                    // user won this auction
                    auc.getBids().get(0).setWinner(true);
                    // check if it was reviewed or not
                    List<HashMap<Integer, String>> review =
                            ConnectionUtil.getMultipleColumns(
                                    "select a.id " +
                                            "from auction_info a, " +
                                            "reviews r " +
                                            "where a.id = r.auction_info_id " +
                                            "and a.id = '" + auc.getAuction_info_id() + "'", 1);
                    if (!review.isEmpty() && review.get(0) != null && review.get(0).get(1) != null) {
                        // the select returned something, meaning the user already reviewed that auction
                    } else {
                        auc.setCanBeReviewed(true);
                    }
                } else {
                    auc.getBids().get(0).setHighest(true);
                }
            }
        });
        return auctionWithBids;
    }

    /**
     * @return {@link List<AuctionInfoDTO>} containing all the actives auctions filtered by the given input parameters
     */
    public List<AuctionInfoDTO> retrieveActiveAuctions(String[] phones_in_auction_array, String startingPrice, String currentPrice, String targetPrice, String sellerName) {
        List<AuctionInfoDTO> activeAuctions = new ArrayList<>();

        String query = "SELECT ai.id, " +
                "phone_names.phone_names, " +
                "ai.starting_price, " +
                "ai.target_price, " +
                "ai.current_price_bidded, " +
                "ai.datetime_start, " +
                "ai.datetime_end," +
                "ai.seller_id, " +
                "u.name, " +
                "ai.additional_info, " +
                "ai.is_successfully_done " +
                "FROM auction_info ai " +
                "INNER JOIN users u " +
                "ON u.id = ai.seller_id " +
                "INNER JOIN (" +
                "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                "FROM phones p, phones_in_auction pa " +
                "WHERE p.id = pa.phone_id " +
                "GROUP BY pa.auction_info_id" +
                ") phone_names " +
                "ON phone_names.auction_info_id = ai.id " +
                "WHERE ai.DATETIME_END >= CURRENT_TIMESTAMP " +
                "AND ai.is_Validated = true";
        if (phones_in_auction_array != null) {
            for (String phone : phones_in_auction_array) {
                query = query + " AND lower(phone_names) like '%" + phone.toLowerCase() + "%'";
            }
        }
        if (startingPrice != null && !startingPrice.isEmpty()) {
            String[] startingPriceSplit = startingPrice.split(",");
            query = query + " AND ai.starting_price >= " + startingPriceSplit[0];
            query = query + " AND ai.starting_price <= " + startingPriceSplit[1];
        }
        if (currentPrice != null && !currentPrice.isEmpty()) {
            String[] currentPriceSplit = currentPrice.split(",");
            query = query + " AND ai.current_price_bidded >= " + currentPriceSplit[0];
            query = query + " AND ai.current_price_bidded <= " + currentPriceSplit[1];
        }
        if (targetPrice != null && !targetPrice.isEmpty()) {
            String[] targetPriceSplit = targetPrice.split(",");
            query = query + " AND ai.target_price >= " + targetPriceSplit[0];
            query = query + " AND ai.target_price <= " + targetPriceSplit[1];
        }
        if (sellerName != null && !sellerName.isEmpty()) {
            query = query + " AND lower(u.name) like '%" + sellerName.toLowerCase() + "%'";
        }

        List<HashMap<Integer, String>> activeAuctionsMap = ConnectionUtil.getMultipleColumns(query, 11);
        return getAuctionInfoDTOS(activeAuctions, activeAuctionsMap);
    }

    /**
     * Method used to retrieve price intervals for filtering auctions under "/auctions" endpoint.
     * @return String[]:
     * 0: targetPriceMin
     * 1: targetPriceMax
     * 2: startingPriceMin
     * 3: startingPriceMax
     * 4: currentPriceMin
     * 5: currentPriceMax
     */
    public String[] retrievePriceIntervals() {
        String query = "SELECT min(target_price) as targetPriceMin, " +
                "max(target_price) as targetPriceMax, " +
                "min(starting_price) as startingPriceMin, " +
                "max(starting_price) as startingPriceMax, " +
                "min(current_price_bidded) as currentPriceMin, " +
                "max(current_price_bidded) as currentPriceMax " +
                "FROM auction_info ai " +
                "WHERE ai.DATETIME_END >= CURRENT_TIMESTAMP " +
                "AND ai.is_Validated = true";
        List<HashMap<Integer, String>> priceIntervalsMap = ConnectionUtil.getMultipleColumns(query, 6);
        String[] result = new String[6];
        HashMap<Integer, String> firstLine = priceIntervalsMap.get(0);
        if (firstLine != null && !firstLine.isEmpty()) {
            String price = firstLine.get(1);
            if (price != null && !price.isEmpty()) {
                result[0] = price;
            } else {
                result[0] = "0";
            }
            price = firstLine.get(2);
            if (price != null && !price.isEmpty()) {
                result[1] = price;
            } else {
                result[1] = "1000";
            }
            price = firstLine.get(3);
            if (price != null && !price.isEmpty()) {
                result[2] = price;
            } else {
                result[2] = "0";
            }
            price = firstLine.get(4);
            if (price != null && !price.isEmpty()) {
                result[3] = price;
            } else {
                result[3] = "1000";
            }
            price = firstLine.get(5);
            if (price != null && !price.isEmpty()) {
                result[4] = price;
            } else {
                result[4] = "0";
            }
            price = firstLine.get(6);
            if (price != null && !price.isEmpty()) {
                result[5] = price;
            } else {
                result[5] = "1000";
            }
        } else {
            // some default values
            result[0] = "0";
            result[1] = "1000";
            result[2] = "0";
            result[3] = "1000";
            result[4] = "0";
            result[5] = "1000";
        }

        return result;
    }

    /**
     * Method used to retrieve price intervals for filtering auctions under "/auctions" endpoint.
     * It is specially built for admins, as they can see the active, ended and not-validated auctions.
     * @return String[]:
     * 0: targetPriceMin
     * 1: targetPriceMax
     * 2: startingPriceMin
     * 3: startingPriceMax
     * 4: currentPriceMin
     * 5: currentPriceMax
     */
    public String[] retrievePriceIntervalsForAdmin() {
        String query = "SELECT min(target_price) as targetPriceMin, " +
                "max(target_price) as targetPriceMax, " +
                "min(starting_price) as startingPriceMin, " +
                "max(starting_price) as startingPriceMax, " +
                "min(current_price_bidded) as currentPriceMin, " +
                "max(current_price_bidded) as currentPriceMax " +
                "FROM auction_info ai";
        List<HashMap<Integer, String>> priceIntervalsMap = ConnectionUtil.getMultipleColumns(query, 6);
        String[] result = new String[6];
        HashMap<Integer, String> firstLine = priceIntervalsMap.get(0);
        if (firstLine != null && !firstLine.isEmpty()) {
            String price = firstLine.get(1);
            if (price != null && !price.isEmpty()) {
                result[0] = price;
            } else {
                result[0] = "0";
            }
            price = firstLine.get(2);
            if (price != null && !price.isEmpty()) {
                result[1] = price;
            } else {
                result[1] = "1000";
            }
            price = firstLine.get(3);
            if (price != null && !price.isEmpty()) {
                result[2] = price;
            } else {
                result[2] = "0";
            }
            price = firstLine.get(4);
            if (price != null && !price.isEmpty()) {
                result[3] = price;
            } else {
                result[3] = "1000";
            }
            price = firstLine.get(5);
            if (price != null && !price.isEmpty()) {
                result[4] = price;
            } else {
                result[4] = "0";
            }
            price = firstLine.get(6);
            if (price != null && !price.isEmpty()) {
                result[5] = price;
            } else {
                result[5] = "1000";
            }
        } else {
            // some default values
            result[0] = "0";
            result[1] = "1000";
            result[2] = "0";
            result[3] = "1000";
            result[4] = "0";
            result[5] = "1000";
        }

        return result;
    }

    /**
     * Method used to retrieve the active auctions of the user specified by parameter {@param userId}
     */
    public List<AuctionInfoDTO> retrieveMyActiveAuctions(String userId) {
        List<AuctionInfoDTO> activeAuctions = new ArrayList<>();
        List<HashMap<Integer, String>> activeAuctionsMap = ConnectionUtil.getMultipleColumns(
                "SELECT ai.id, " +
                        "phone_names.phone_names, " +
                        "ai.starting_price, " +
                        "ai.target_price, " +
                        "ai.current_price_bidded, " +
                        "ai.datetime_start, " +
                        "ai.datetime_end," +
                        "ai.seller_id, " +
                        "u.name, " +
                        "ai.additional_info, " +
                        "ai.is_successfully_done " +
                        "FROM auction_info ai " +
                        "INNER JOIN users u " +
                        "ON u.id = ai.seller_id " +
                        "INNER JOIN (" +
                        "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                        "FROM phones p, phones_in_auction pa " +
                        "WHERE p.id = pa.phone_id " +
                        "GROUP BY pa.auction_info_id" +
                        ") phone_names " +
                        "ON phone_names.auction_info_id = ai.id " +
                        "WHERE ai.DATETIME_END >= CURRENT_TIMESTAMP " +
                        "AND ai.is_Validated = true " +
                        "AND u.id = '" + userId + "'",
                11);
        return getAuctionInfoDTOS(activeAuctions, activeAuctionsMap);
    }

    /**
     * Method used to retrieve the all the inactive auctions.
     */
    public List<AuctionInfoDTO> retrieveInactiveAuctions() {
        List<AuctionInfoDTO> allAuctions = new ArrayList<>();
        List<AuctionInfoDTO> activeAuctions = retrieveActiveAuctions();
        List<HashMap<Integer, String>> allAuctionsMap = ConnectionUtil.getMultipleColumns(
                "SELECT ai.id, " +
                        "phone_names.phone_names, " +
                        "ai.starting_price, " +
                        "ai.target_price, " +
                        "ai.current_price_bidded, " +
                        "ai.datetime_start, " +
                        "ai.datetime_end," +
                        "ai.seller_id, " +
                        "u.name, " +
                        "ai.additional_info, " +
                        "ai.is_successfully_done " +
                        "FROM auction_info ai " +
                        "INNER JOIN users u " +
                        "ON u.id = ai.seller_id " +
                        "INNER JOIN (" +
                        "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                        "FROM phones p, phones_in_auction pa " +
                        "WHERE p.id = pa.phone_id " +
                        "GROUP BY pa.auction_info_id" +
                        ") phone_names " +
                        "ON phone_names.auction_info_id = ai.id " +
                        "WHERE ai.is_Validated = true",
                11);
        getAuctionInfoDTOS(allAuctions, allAuctionsMap);

        for (AuctionInfoDTO auction : new ArrayList<>(allAuctions)) {
            for (AuctionInfoDTO activeAuction : activeAuctions) {
                if (activeAuction.getId().equals(auction.getId())) {
                    allAuctions.remove(auction);
                    break;
                }
            }
        }
        return allAuctions;
    }

    /**
     * Method used to retrieve the inactive auctions filtered by the input parameters.
     */
    public List<AuctionInfoDTO> retrieveInactiveAuctions(String[] phones_in_auction_array, String startingPrice, String currentPrice, String targetPrice, String sellerName) {
        List<AuctionInfoDTO> allAuctions = new ArrayList<>();
        List<AuctionInfoDTO> activeAuctions = retrieveActiveAuctions(phones_in_auction_array, startingPrice, currentPrice, targetPrice, sellerName);
        String query = "SELECT ai.id, " +
                "phone_names.phone_names, " +
                "ai.starting_price, " +
                "ai.target_price, " +
                "ai.current_price_bidded, " +
                "ai.datetime_start, " +
                "ai.datetime_end," +
                "ai.seller_id, " +
                "u.name, " +
                "ai.additional_info, " +
                "ai.is_successfully_done " +
                "FROM auction_info ai " +
                "INNER JOIN users u " +
                "ON u.id = ai.seller_id " +
                "INNER JOIN (" +
                "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                "FROM phones p, phones_in_auction pa " +
                "WHERE p.id = pa.phone_id " +
                "GROUP BY pa.auction_info_id" +
                ") phone_names " +
                "ON phone_names.auction_info_id = ai.id " +
                "WHERE ai.is_Validated = true";
        if (phones_in_auction_array != null) {
            for (String phone : phones_in_auction_array) {
                query = query + " AND lower(phone_names) like '%" + phone.toLowerCase() + "%'";
            }
        }
        if (startingPrice != null && !startingPrice.isEmpty()) {
            String[] startingPriceSplit = startingPrice.split(",");
            query = query + " AND ai.starting_price >= " + startingPriceSplit[0];
            query = query + " AND ai.starting_price <= " + startingPriceSplit[1];
        }
        if (currentPrice != null && !currentPrice.isEmpty()) {
            String[] currentPriceSplit = currentPrice.split(",");
            query = query + " AND ai.current_price_bidded >= " + currentPriceSplit[0];
            query = query + " AND ai.current_price_bidded <= " + currentPriceSplit[1];
        }
        if (targetPrice != null && !targetPrice.isEmpty()) {
            String[] targetPriceSplit = targetPrice.split(",");
            query = query + " AND ai.target_price >= " + targetPriceSplit[0];
            query = query + " AND ai.target_price <= " + targetPriceSplit[1];
        }
        if (sellerName != null && !sellerName.isEmpty()) {
            query = query + " AND lower(u.name) like '%" + sellerName.toLowerCase() + "%'";
        }
        List<HashMap<Integer, String>> allAuctionsMap = ConnectionUtil.getMultipleColumns(query, 11);
        getAuctionInfoDTOS(allAuctions, allAuctionsMap);

        for (AuctionInfoDTO auction : new ArrayList<>(allAuctions)) {
            for (AuctionInfoDTO activeAuction : activeAuctions) {
                if (activeAuction.getId().equals(auction.getId())) {
                    allAuctions.remove(auction);
                    break;
                }
            }
        }
        return allAuctions;
    }

    /**
     * Method used to retrieve the inactive auctions of the user specified by parameter {@param userId}
     */
    public List<AuctionInfoDTO> retrieveMyInactiveAuctions(String userId) {
        List<AuctionInfoDTO> allAuctions = new ArrayList<>();
        List<AuctionInfoDTO> activeAuctions = retrieveActiveAuctions();
        List<HashMap<Integer, String>> allAuctionsMap = ConnectionUtil.getMultipleColumns(
                "SELECT ai.id, " +
                        "phone_names.phone_names, " +
                        "ai.starting_price, " +
                        "ai.target_price, " +
                        "ai.current_price_bidded, " +
                        "ai.datetime_start, " +
                        "ai.datetime_end," +
                        "ai.seller_id, " +
                        "u.name, " +
                        "ai.additional_info, " +
                        "ai.is_successfully_done " +
                        "FROM auction_info ai " +
                        "INNER JOIN users u " +
                        "ON u.id = ai.seller_id " +
                        "INNER JOIN (" +
                        "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                        "FROM phones p, phones_in_auction pa " +
                        "WHERE p.id = pa.phone_id " +
                        "GROUP BY pa.auction_info_id" +
                        ") phone_names " +
                        "ON phone_names.auction_info_id = ai.id " +
                        "WHERE ai.is_Validated = true " +
                        "and u.id = '" + userId + "'",
                11);
        getAuctionInfoDTOS(allAuctions, allAuctionsMap);

        for (AuctionInfoDTO auction : new ArrayList<>(allAuctions)) {
            for (AuctionInfoDTO activeAuction : activeAuctions) {
                if (activeAuction.getId().equals(auction.getId())) {
                    allAuctions.remove(auction);
                    break;
                }
            }
        }
        return allAuctions;
    }

    /**
     * Method used to retrieve all the not validated auctions.
     */
    public List<AuctionInfoDTO> retrieveNotValidatedAuctions() {
        List<AuctionInfoDTO> allAuctions = new ArrayList<>();
        List<HashMap<Integer, String>> allAuctionsMap = ConnectionUtil.getMultipleColumns(
                "SELECT ai.id, " +
                        "phone_names.phone_names, " +
                        "ai.starting_price, " +
                        "ai.target_price, " +
                        "ai.current_price_bidded, " +
                        "ai.datetime_start, " +
                        "ai.datetime_end," +
                        "ai.seller_id, " +
                        "u.name, " +
                        "ai.additional_info, " +
                        "ai.is_successfully_done " +
                        "FROM auction_info ai " +
                        "INNER JOIN users u " +
                        "ON u.id = ai.seller_id " +
                        "INNER JOIN (" +
                        "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                        "FROM phones p, phones_in_auction pa " +
                        "WHERE p.id = pa.phone_id " +
                        "GROUP BY pa.auction_info_id" +
                        ") phone_names " +
                        "ON phone_names.auction_info_id = ai.id " +
                        "WHERE ai.is_validated = false;",
                11);
        return getAuctionInfoDTOS(allAuctions, allAuctionsMap);
    }

    /**
     * Method used to retrieve all the not validated auctions filtered by the input parameters.
     */
    public List<AuctionInfoDTO> retrieveNotValidatedAuctions(String[] phones_in_auction_array, String startingPrice, String currentPrice, String targetPrice, String sellerName) {
        List<AuctionInfoDTO> allAuctions = new ArrayList<>();
        String query = "SELECT ai.id, " +
                "phone_names.phone_names, " +
                "ai.starting_price, " +
                "ai.target_price, " +
                "ai.current_price_bidded, " +
                "ai.datetime_start, " +
                "ai.datetime_end," +
                "ai.seller_id, " +
                "u.name, " +
                "ai.additional_info " +
                "FROM auction_info ai " +
                "INNER JOIN users u " +
                "ON u.id = ai.seller_id " +
                "INNER JOIN (" +
                "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                "FROM phones p, phones_in_auction pa " +
                "WHERE p.id = pa.phone_id " +
                "GROUP BY pa.auction_info_id" +
                ") phone_names " +
                "ON phone_names.auction_info_id = ai.id " +
                "WHERE ai.is_validated = false ";
        if (phones_in_auction_array != null) {
            for (String phone : phones_in_auction_array) {
                query = query + " AND lower(phone_names) like '%" + phone.toLowerCase() + "%'";
            }
        }
        if (startingPrice != null && !startingPrice.isEmpty()) {
            String[] startingPriceSplit = startingPrice.split(",");
            query = query + " AND ai.starting_price >= " + startingPriceSplit[0];
            query = query + " AND ai.starting_price <= " + startingPriceSplit[1];
        }
        if (currentPrice != null && !currentPrice.isEmpty()) {
            String[] currentPriceSplit = currentPrice.split(",");
            query = query + " AND ai.current_price_bidded >= " + currentPriceSplit[0];
            query = query + " AND ai.current_price_bidded <= " + currentPriceSplit[1];
        }
        if (targetPrice != null && !targetPrice.isEmpty()) {
            String[] targetPriceSplit = targetPrice.split(",");
            query = query + " AND ai.target_price >= " + targetPriceSplit[0];
            query = query + " AND ai.target_price <= " + targetPriceSplit[1];
        }
        if (sellerName != null && !sellerName.isEmpty()) {
            query = query + " AND lower(u.name) like '%" + sellerName.toLowerCase() + "%'";
        }
        List<HashMap<Integer, String>> allAuctionsMap = ConnectionUtil.getMultipleColumns(query, 10);
        return getAuctionInfoDTOS(allAuctions, allAuctionsMap);
    }

    /**
     * Method used to retrieve the not-validated auctions of the user specified by parameter {@param userId}
     */
    public List<AuctionInfoDTO> retrieveMyNotValidatedAuctions(String userId) {
        List<AuctionInfoDTO> allAuctions = new ArrayList<>();
        List<HashMap<Integer, String>> allAuctionsMap = ConnectionUtil.getMultipleColumns(
                "SELECT ai.id, " +
                        "phone_names.phone_names, " +
                        "ai.starting_price, " +
                        "ai.target_price, " +
                        "ai.current_price_bidded, " +
                        "ai.datetime_start, " +
                        "ai.datetime_end," +
                        "ai.seller_id, " +
                        "u.name, " +
                        "ai.additional_info, " +
                        "ai.is_successfully_done " +
                        "FROM auction_info ai " +
                        "INNER JOIN users u " +
                        "ON u.id = ai.seller_id " +
                        "INNER JOIN (" +
                        "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                        "FROM phones p, phones_in_auction pa " +
                        "WHERE p.id = pa.phone_id " +
                        "GROUP BY pa.auction_info_id" +
                        ") phone_names " +
                        "ON phone_names.auction_info_id = ai.id " +
                        "WHERE ai.is_validated = false " +
                        "and u.id = '" + userId + "'",
                11);
        return getAuctionInfoDTOS(allAuctions, allAuctionsMap);
    }

    /**
     * Helper method to convert from data from the database to objects.
     */
    private List<AuctionInfoDTO> getAuctionInfoDTOS(List<AuctionInfoDTO> activeAuctions, List<HashMap<Integer, String>> activeAuctionsMap) {
        activeAuctionsMap.forEach(map -> {
            AuctionInfoDTO auctionInfoDTO = new AuctionInfoDTO();
            auctionInfoDTO.setId(map.get(1));
            auctionInfoDTO.setTitle(map.get(2));
            auctionInfoDTO.setStarting_price(Double.parseDouble(map.get(3)));
            auctionInfoDTO.setTarget_price(Double.parseDouble(map.get(4)));
            auctionInfoDTO.setCurrent_price_bidded(Double.parseDouble(map.get(5)));
            auctionInfoDTO.setDatetime_start(map.get(6));
            auctionInfoDTO.setDatetime_end(map.get(7));
            auctionInfoDTO.setSeller_id(map.get(8));
            auctionInfoDTO.setSeller_name(map.get(9));
            auctionInfoDTO.setAdditional_info(map.get(10));
            auctionInfoDTO.setIs_successfully_done(Boolean.parseBoolean(map.get(11)));
            activeAuctions.add(auctionInfoDTO);
        });
        return activeAuctions;
    }

    /**
     * Method used to delete the auction specified by parameter {@param id}.
     * This also deletes all the information from the database regarding the auction.
     */
    public void deleteAuction(String id) {
        ConnectionUtil.parseUpdateQuery("DELETE FROM auction_info WHERE id = '" + id + "';");
        ConnectionUtil.parseUpdateQuery("DELETE FROM phones_in_auction WHERE auction_info_id = '" + id + "';");
    }

    /**
     * Method used to validate the auction specified by parameter {@param id}.
     */
    public void validateAuction(String id) {
        ConnectionUtil.parseUpdateQuery("UPDATE auction_info SET is_Validated = true WHERE id = '" + id + "'");
    }

    /**
     * Method used to create a new auction with the information specified by the input parameters.
     */
    public void createAuction(String phones_in_auction, String datetime_end, String additional_info,
                              String starting_price, String target_price) {
        String auction_info_id = UUID.randomUUID().toString();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime datetime_start = LocalDateTime.now();
        ConnectionUtil.parseUpdateQuery("INSERT INTO auction_info (id, datetime_start, datetime_end, is_validated, seller_id, additional_info, starting_price, target_price, current_price_bidded) VALUES " +
                "('" + auction_info_id + "', parsedatetime('" + dtf.format(datetime_start) + "', 'dd-MM-yyyy hh:mm:ss')" + ", parsedatetime('" + datetime_end + "', 'dd-MM-yyyy hh:mm:ss')" + ", false, '" + userService.getUserId() + "', '" + additional_info + "', " + starting_price + ", " + target_price + ", " + 0 + ")");

        String[] phones_in_auction_array = phones_in_auction.split(",");
        String query = "INSERT INTO PHONES_IN_AUCTION (id, auction_info_id, phone_id) VALUES ";
        for (String phone_id : phones_in_auction_array) {
            query += "('" + UUID.randomUUID().toString() + "', '" + auction_info_id + "', '" + phone_id + "'),";
        }
        if (query.endsWith(",")) {
            query = query.substring(0, query.length() - 1);
        }
        ConnectionUtil.parseUpdateQuery(query);

        scheduleAuctionEnd(auction_info_id, LocalDateTime.parse(datetime_end, dtf));
    }

    /**
     * @return {@link List<AvailablePhonesDTO>} containing all the available phones that can be added to an auction.
     */
    public List<AvailablePhonesDTO> retrieveAvailablePhones() {
        List<AvailablePhonesDTO> availablePhonesDTOList = new ArrayList<>();
        List<HashMap<Integer, String>> phones =
                ConnectionUtil.getMultipleColumns("SELECT id, phone_brand || ' ' || phone_model as phoneName FROM phones;", 2);
        phones.forEach(phone -> {
            availablePhonesDTOList.add(new AvailablePhonesDTO(phone.get(1), phone.get(2), false));
        });
        return availablePhonesDTOList;
    }

    /**
     * @return {@link List<AvailablePhonesDTO>} containing all the available phones that can be added to an auction,
     * without specifying the phone brand.
     */
    public List<AvailablePhonesDTO> retrieveAvailablePhonesWithoutBrand() {
        List<AvailablePhonesDTO> availablePhonesDTOList = new ArrayList<>();
        List<HashMap<Integer, String>> phones =
                ConnectionUtil.getMultipleColumns("SELECT id, phone_model as phoneName FROM phones;", 2);
        phones.forEach(phone -> {
            availablePhonesDTOList.add(new AvailablePhonesDTO(phone.get(1), phone.get(2), false));
        });
        return availablePhonesDTOList;
    }

    /**
     * @return {@link List<AvailablePhonesDTO>} containing all the available phones that can be added to an auction, but also the ones
     * that are already added to the auction specified by the input parameter {@param auction_info_id}.
     */
    public List<AvailablePhonesDTO> retrieveAvailablePhonesAndPhonesInAuction(String auction_info_id) {
        List<AvailablePhonesDTO> availablePhonesDTOList = new ArrayList<>();
        List<String> phonesInAuction = retrievePhonesInAuction(auction_info_id);
        List<HashMap<Integer, String>> phones =
                ConnectionUtil.getMultipleColumns("SELECT id, phone_brand || ' ' || phone_model as phoneName FROM phones;", 2);
        phones.forEach(phone -> {
            boolean isSelected = false;
            for (String phoneInAuction : phonesInAuction) {
                if (phoneInAuction.equals(phone.get(1))) {
                    isSelected = true;
                }
            }
            availablePhonesDTOList.add(new AvailablePhonesDTO(phone.get(1), phone.get(2), isSelected));
        });
        return availablePhonesDTOList;
    }

    /**
     * @return {@link List<String>} containing all the phones that are already added to the auction,
     * specified by the input parameter {@param auction_info_id}.
     */
    public List<String> retrievePhonesInAuction(String auction_info_id) {
        List<String> phoneInAuctionList = new ArrayList<>();
        List<HashMap<Integer, String>> phonesInAuction =
                ConnectionUtil.getMultipleColumns("SELECT phone_id FROM phones_in_auction WHERE auction_info_id = '" + auction_info_id + "';", 1);
        phonesInAuction.forEach(phone -> {
            phoneInAuctionList.add(phone.get(1));
        });
        return phoneInAuctionList;
    }

    /**
     * @return {@link HashMap} containing the auction information specified by the input parameter {@param auction_info_id}.
     */
    public Map<String, String> getAuctionInfo(String auction_info_id) {
        Map<String, String> map = new HashMap<>();
        List<HashMap<Integer, String>> auctionList = ConnectionUtil.getMultipleColumns(
                "SELECT ai.starting_price, " +
                        "ai.target_price, " +
                        "ai.datetime_end, " +
                        "ai.additional_info " +
                        "FROM auction_info ai " +
                        "WHERE ai.id = '" + auction_info_id + "'",
                4);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        DateTimeFormatter dbDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        HashMap<Integer, String> auctionInfo = auctionList.get(0);
        map.put("starting_price", auctionInfo.get(1));
        map.put("target_price", auctionInfo.get(2));
        map.put("datetime_end", LocalDateTime.parse(auctionInfo.get(3), dbDtf).format(dtf));
        map.put("additional_info", auctionInfo.get(4));
        return map;
    }

    /**
     * @param auction_info_id the id of the auction
     * @return {@link boolean} stating whether or not the auction is accessible by the current user (the user created it or not)
     */
    public boolean userHasAccessToAuction(String auction_info_id) {
        List<HashMap<Integer, String>> auctionList = ConnectionUtil.getMultipleColumns(
                "SELECT ai.id " +
                        "FROM auction_info ai " +
                        "WHERE ai.id = '" + auction_info_id + "' " +
                        "AND ai.seller_id = '" + userService.getUserId() + "'",
                1);
        return !auctionList.isEmpty() && auctionList.get(0) != null && !auctionList.get(0).isEmpty() && auctionList.get(0).get(1) != null;
    }

    /**
     * Method used to edit an already existing auction specified by the input parameters.
     */
    public void updateAuction(String auction_info_id, String phones_in_auction, String datetime_end, String additional_info,
                              String starting_price, String target_price) {
        additional_info = additional_info.replaceAll("'", "''");
        ConnectionUtil.parseUpdateQuery("UPDATE auction_info SET is_validated = false, datetime_end = parsedatetime('" + datetime_end + "', 'dd-MM-yyyy hh:mm:ss')" + ", additional_info = '" + additional_info + "', starting_price = '" + starting_price + "', target_price = '" + target_price + "' WHERE id = '" + auction_info_id + "'");
        ConnectionUtil.parseUpdateQuery("DELETE FROM phones_in_auction WHERE auction_info_id = '" + auction_info_id + "'");

        String[] phones_in_auction_array = phones_in_auction.split(",");
        String query = "INSERT INTO PHONES_IN_AUCTION (id, auction_info_id, phone_id) VALUES ";
        for (String phone_id : phones_in_auction_array) {
            query += "('" + UUID.randomUUID().toString() + "', '" + auction_info_id + "', '" + phone_id + "'),";
        }
        if (query.endsWith(",")) {
            query = query.substring(0, query.length() - 1);
        }
        ConnectionUtil.parseUpdateQuery(query);
    }

    /**
     * Method used to create a new phone.
     */
    public void createPhone(String phone_brand, String phone_model) {
        ConnectionUtil.parseUpdateQuery("INSERT INTO PHONES (id, phone_brand, phone_model) VALUES " +
                "('" + java.util.UUID.randomUUID().toString() + "', '" + phone_brand + "', '" + phone_model + "'),");
    }

    /**
     * @param auctionInfoDTO object containing information about the auction
     * @return {@link boolean} stating whether or not the auction ca be completed manually (an auction that haven't reached
     * the target price can be completed manually by the auction creator)
     */
    public boolean canBeManuallyDone(AuctionInfoDTO auctionInfoDTO) {
        if (!userService.getUserId().equals(auctionInfoDTO.getSeller_id())) {
            // only the seller can manually end auction
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dbDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime auctionEndDateTime = LocalDateTime.parse(auctionInfoDTO.getDatetime_end(), dbDtf);
        // if the auction has ended (end time passed) and hasn't been automatically assigned a winner
        return auctionEndDateTime.isBefore(now) && !auctionInfoDTO.isIs_successfully_done();
    }

    /**
     * The current user can bid to the auction only if it is not the admin, it is not the auction creator, the auction has not ended
     * and the last bid (the maximum) is not already his/her bid.
     * @param auctionInfoDTO object containing information about the auction
     * @return {@link boolean} stating whether or not the current user can bid to the auction
     */
    public boolean canBid(AuctionInfoDTO auctionInfoDTO) {
        if (userService.isUserAdmin()) {
            return false;
        }
        if (userService.getUserId().equals(auctionInfoDTO.getSeller_id())) {
            // seller cannot bid to his/her auction
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dbDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime auctionEndDateTime = LocalDateTime.parse(auctionInfoDTO.getDatetime_end(), dbDtf);
        if (auctionEndDateTime.isBefore(now)) {
            // if the auction has ended (end time passed), you cannot bid anymore
            return false;
        }
        String query =
                "SELECT b.buyer_id " +
                        "FROM AUCTION_INFO a " +
                        "inner join bids b " +
                        "on a.id = b.auction_info_id " +
                        "and a.current_price_bidded = b.price_bidded " +
                        "and a.id = '" + auctionInfoDTO.getId() + "'";
        List<HashMap<Integer, String>> asd = ConnectionUtil.getMultipleColumns(query, 1);
        // if the last bidder is the current user, there is no sense to bid again
        return asd.isEmpty() || asd.get(0) == null || asd.get(0).isEmpty() || asd.get(0).get(1) == null || !asd.get(0).get(1).equals(userService.getUserId());
    }

    /**
     * The conditions for a successful bid is contained in  {@link AuctionService#canBid(AuctionInfoDTO)} and also,
     * the new bid must be greater than the current maximum bid + 5% of the target price of the auction.
     *
     * @param auctionId the id of the action contained in auction_info table
     * @param bidAmount the amount the user is trying to bid for this auction
     * @return String containing the error message (if couldn't bid) or an "OK" marking a successful bid
     */
    public String bid(String auctionId, String bidAmount) {
        AuctionInfoDTO auctionInfoDTO = retrieveAuctionInfo(auctionId);

        if (canBid(auctionInfoDTO)) {
            double newBid = Double.parseDouble(bidAmount);
            double minimumBidIncrement = auctionInfoDTO.getTarget_price() * 0.05;
            if (newBid < auctionInfoDTO.getCurrent_price_bidded() + minimumBidIncrement) {
                return "A new bid to this auction requires to be at least the current highest bid + 5% of the target price of the auction (" + minimumBidIncrement + ")!";
            }
            String query =
                    "update auction_info " +
                            "set current_price_bidded = " + newBid +
                            " where id = '" + auctionId + "'";
            ConnectionUtil.parseUpdateQuery(query);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime datetimeBidded = LocalDateTime.now();
            query = "INSERT INTO bids (id, auction_info_id, buyer_id, datetime_bidded, price_bidded) VALUES " +
                    "('" + UUID.randomUUID().toString() + "', '" + auctionId + "', '" + userService.getUserId() + "', parsedatetime('" + dtf.format(datetimeBidded) + "', 'dd-MM-yyyy hh:mm:ss')" + ", " + newBid + ")";
            ConnectionUtil.parseUpdateQuery(query);
            return "OK";
        } else {
            return "The auction has ended or you cannot bid anymore!";
        }
    }

    /**
     * This method will try to manually complete an auction as requested by the user.
     * Will only work if all the following requirements are met:
     * 1. the end datetime has passed
     * 2. the auction is not already completed - i.e. it doesn't already have a winner
     *
     * @param id the id of the action contained in auction_info table
     * @return a boolean stating whether the auction has been successfully completed manually
     */
    public boolean manuallyDoAuction(String id) {
        AuctionInfoDTO auctionInfoDTO = retrieveAuctionInfo(id);

        if (canBeManuallyDone(auctionInfoDTO)) {
            String query =
                    "update auction_info " +
                            "set is_successfully_done = true " +
                            "where id = '" + id + "'";
            ConnectionUtil.parseUpdateQuery(query);
            return true;
        }

        return false;
    }

    /**
     * This method will schedule a job (see {@link AuctionService.AuctionHandler}) to be executed at auction ending
     * datetime. This will be successfully scheduled only if the auction is not already ended.
     * @param id the id of the auction that will be scheduled for ending
     * @param endDateTime the date of the auction ending
     */
    public void scheduleAuctionEnd(String id, LocalDateTime endDateTime) {
        LocalDateTime now = LocalDateTime.now();
        if (endDateTime.isAfter(now)) {
            executor.schedule(new AuctionHandler(id), now.until(endDateTime, ChronoUnit.SECONDS), TimeUnit.SECONDS);
        }
    }

    /**
     * Class implementing the {@link Runnable} interface, used for ending an auction.
     */
    class AuctionHandler implements Runnable {
        String auctionId;

        public AuctionHandler(String auctionId) {
            this.auctionId = auctionId;
        }

        /**
         * This method will run at the scheduled time (auction ending datetime) and will update the information in the auction_info table.
         * If the auction specified by {@link AuctionHandler#auctionId} has a bid greater than the target price, then the auction is ended
         * successfully and marked by setting the is_successfully_done flag to true in the database.
         */
        @Override
        public void run() {
            String query =
                    "update auction_info " +
                            "set is_successfully_done = true " +
                            "where id = " +
                            "(select a.id " +
                            "from auction_info a " +
                            "inner join " +
                            "(select max(price_bidded) as maxbidded_price, auction_info_id " +
                            "from bids " +
                            "group by auction_info_id) b " +
                            "on a.id = b.auction_info_id " +
                            "where a.id ='" + this.auctionId + "' " +
                            "and b.maxbidded_price >= a.target_price);";
            ConnectionUtil.parseUpdateQuery(query);
        }
    }
}
