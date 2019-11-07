package com.onlinephoneauctions.service;

import com.onlinephoneauctions.dbconnect.ConnectionUtil;
import com.onlinephoneauctions.dto.AuctionInfoDTO;
import com.onlinephoneauctions.dto.AvailablePhonesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AuctionService {
    @Autowired
    private UserService userService;

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
                        "WHERE ai.DATETIME_END >= CURRENT_TIMESTAMP " +
                        "AND ai.is_Validated = true",
                10);
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
            activeAuctions.add(auctionInfoDTO);
        });
        return activeAuctions;
    }

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
                        "WHERE ai.is_Validated = true",
                10);
        allAuctionsMap.forEach(map -> {
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
            allAuctions.add(auctionInfoDTO);
        });

        for (AuctionInfoDTO auction : new ArrayList<>(allAuctions)) {
            for (AuctionInfoDTO activeAuction : activeAuctions) {
                if(activeAuction.getId().equals(auction.getId())) {
                    allAuctions.remove(auction);
                    break;
                }
            }
        }
        return allAuctions;
    }

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
                        "WHERE ai.is_validated = false;",
                10);
        allAuctionsMap.forEach(map -> {
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
            allAuctions.add(auctionInfoDTO);
        });

        return allAuctions;
    }

    public void deleteAuction(String id) {
        ConnectionUtil.parseUpdateQuery("DELETE FROM auction_info WHERE id = '" + id + "';");
        ConnectionUtil.parseUpdateQuery("DELETE FROM phones_in_auction WHERE auction_info_id = '" + id + "';");
    }

    public void validateAuction(String id) {
        ConnectionUtil.parseUpdateQuery("UPDATE auction_info SET is_Validated = true WHERE id = '" + id + "'");
    }

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
    }

    public List<AvailablePhonesDTO> retrieveAvailablePhones() {
        List<AvailablePhonesDTO> availablePhonesDTOList = new ArrayList<>();
        List<HashMap<Integer, String>> phones =
                ConnectionUtil.getMultipleColumns("SELECT id, phone_brand || ' ' || phone_model as phoneName FROM phones;", 2);
        phones.forEach(phone -> {
            availablePhonesDTOList.add(new AvailablePhonesDTO(phone.get(1), phone.get(2), false));
        });
        return availablePhonesDTOList;
    }

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

    public List<String> retrievePhonesInAuction(String auction_info_id) {
        List<String> phoneInAuctionList = new ArrayList<>();
        List<HashMap<Integer, String>> phonesInAuction =
                ConnectionUtil.getMultipleColumns("SELECT phone_id FROM phones_in_auction WHERE auction_info_id = '" + auction_info_id + "';", 1);
        phonesInAuction.forEach(phone -> {
            phoneInAuctionList.add(phone.get(1));
        });
        return phoneInAuctionList;
    }

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

    public boolean userHasAccessToAuction(String auction_info_id) {
        List<HashMap<Integer, String>> auctionList = ConnectionUtil.getMultipleColumns(
                "SELECT ai.id " +
                        "FROM auction_info ai " +
                        "WHERE ai.id = '" + auction_info_id + "' " +
                        "AND ai.seller_id = '" + userService.getUserId() + "'",
                1);
        return !auctionList.isEmpty() && auctionList.get(0) != null && !auctionList.get(0).isEmpty() && auctionList.get(0).get(1) != null;
    }

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

    public void createPhone(String phone_brand, String phone_model) {
        ConnectionUtil.parseUpdateQuery("INSERT INTO PHONES (id, phone_brand, phone_model) VALUES " +
                "('" + java.util.UUID.randomUUID().toString() + "', '" + phone_brand + "', '" + phone_model + "'),");
    }
}
