package com.onlinephoneauctions.service;

import com.onlinephoneauctions.dbconnect.ConnectionUtil;
import com.onlinephoneauctions.dto.AuctionInfoDTO;
import com.onlinephoneauctions.dto.AvailablePhonesDTO;
import org.apache.tomcat.jni.Local;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.onlinephoneauctions.security.WebSecurityConfig.passwordEncoder;

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
                            "ai.datetime_end, " +
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
                        "WHERE ai.DATETIME_END >= CURRENT_TIMESTAMP",
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

    public void deleteAuction(String id) {
        List<HashMap<Integer, String>> ai =
                ConnectionUtil.getMultipleColumns("SELECT seller_id FROM auction_info WHERE id = '" + id + "'", 1);
        if(ai.isEmpty() ||
                ai.get(0) == null ||
                ai.get(0).isEmpty() ||
                ai.get(0).get(1) == null ||
                !ai.get(0).get(1).equals(userService.getUserId())) {
            return;
        }
        ConnectionUtil.parseUpdateQuery("DELETE FROM auction_info WHERE id = '" + id + "';");
        ConnectionUtil.parseUpdateQuery("DELETE FROM phones_in_auction WHERE auction_info_id = '" + id + "';");
    }

    public void createAuction(String phones_in_auction, String datetime_end, String additional_info,
                              String starting_price, String target_price) {
        String auction_info_id = UUID.randomUUID().toString();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime datetime_start = LocalDateTime.now();
        ConnectionUtil.parseUpdateQuery("INSERT INTO auction_info (id, datetime_start, datetime_end, seller_id, additional_info, starting_price, target_price, current_price_bidded) VALUES " +
                "('" + auction_info_id + "', parsedatetime('" + dtf.format(datetime_start) + "', 'dd-MM-yyyy hh:mm:ss')" + ", parsedatetime('" + datetime_end + "', 'dd-MM-yyyy hh:mm:ss')" + ", '" + userService.getUserId() + "', '"  + additional_info + "', " + starting_price + ", " + target_price + ", " + 0 + ")");

        String[] phones_in_auction_array = phones_in_auction.split(",");
        String query = "INSERT INTO PHONES_IN_AUCTION (id, auction_info_id, phone_id) VALUES ";
        for (String phone_id : phones_in_auction_array) {
            query += "('" + UUID.randomUUID().toString() + "', '" + auction_info_id + "', '" + phone_id + "'),";
        }
        if(query.endsWith(",")) {
            query = query.substring(0,query.length() - 1);
        }
        ConnectionUtil.parseUpdateQuery(query);
    }

    public List<AvailablePhonesDTO> retrieveAvailablePhones() {
        List<AvailablePhonesDTO> availablePhonesDTOList= new ArrayList<>();
        List<HashMap<Integer, String>> phones =
            ConnectionUtil.getMultipleColumns("SELECT id, phone_brand || ' ' || phone_model as phoneName FROM phones;", 2);
        phones.forEach(phone -> {
            availablePhonesDTOList.add(new AvailablePhonesDTO(phone.get(1), phone.get(2), false));
        });
        return availablePhonesDTOList;
    }
}
