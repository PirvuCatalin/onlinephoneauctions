package com.onlinephoneauctions.config;

import com.onlinephoneauctions.dbconnect.ConnectionUtil;
import com.onlinephoneauctions.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class OnApplicationStartUp {
    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    private AuctionService auctionService;

    /**
     * This method will automatically execute on application start-up.
     *
     * @param event the parameter marking the (re)start of the server (not used though)
     */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        addUsersToAuthenticationManager();
        scheduleEndingForExistingAuctions();
    }

    /**
     * This will add the users from the database to the in-memory user management of Spring Security.
     * Also, it will assign the ADMIN role to the user named "admin".
     */
    private void addUsersToAuthenticationManager() {
        List<HashMap<Integer, String>> credentials = ConnectionUtil.
                getMultipleColumns("SELECT username, password FROM user_credentials", 2);
        credentials.forEach(map -> {
            if (map.get(1).equals("admin")) {
                List<SimpleGrantedAuthority> list = new ArrayList<>();
                list.add(new SimpleGrantedAuthority("USER"));
                list.add(new SimpleGrantedAuthority("ADMIN"));
                inMemoryUserDetailsManager.createUser(
                        new User(map.get(1), map.get(2), list)
                );
            } else {
                List<SimpleGrantedAuthority> list = new ArrayList<>();
                list.add(new SimpleGrantedAuthority("USER"));
                inMemoryUserDetailsManager.createUser(
                        new User(map.get(1), map.get(2), list)
                );
            }
        });
    }

    /**
     * This method will schedule the ending of all the already existing auctions in the database.
     */
    private void scheduleEndingForExistingAuctions() {
        List<HashMap<Integer, String>> auctions = ConnectionUtil.
                getMultipleColumns("SELECT id, datetime_end FROM auction_info", 2);
        DateTimeFormatter dbDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        auctions.forEach(map -> {
            auctionService.scheduleAuctionEnd(map.get(1), LocalDateTime.parse(map.get(2), dbDtf));
        });
    }
}