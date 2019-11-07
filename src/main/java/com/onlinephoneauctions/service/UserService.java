package com.onlinephoneauctions.service;

import com.onlinephoneauctions.dbconnect.ConnectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static com.onlinephoneauctions.security.WebSecurityConfig.passwordEncoder;

@Service
public class UserService {

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public String getLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    public boolean isUserAdmin() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            for (GrantedAuthority authority : ((UserDetails) principal).getAuthorities()) {
                if(authority.getAuthority().equals("ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, String> getUserInfo() {
        Map<String, String> map = new HashMap<>();

        List<HashMap<Integer, String>> userInfoList =
                ConnectionUtil.getMultipleColumns("SELECT name, birthday, address_detail, city, country FROM users " +
                                "WHERE id = '" + getUserId() + "'",
                        5);
        HashMap<Integer, String> userInfo = userInfoList.get(0);
        map.put("name", userInfo.get(1));
        map.put("birthday", userInfo.get(2));
        map.put("address_detail", userInfo.get(3));
        map.put("city", userInfo.get(4));
        map.put("country", userInfo.get(5));

        return map;
    }

    public Map<String, String> getUserCardInfo() {
        Map<String, String> map = new HashMap<>();

        List<HashMap<Integer, String>> userInfoList =
                ConnectionUtil.getMultipleColumns("SELECT card_number, card_expiry_date, card_cvv, cardholder_name FROM cards " +
                                "WHERE id IN (SELECT cards_id FROM users WHERE id = '" + getUserId() + "')", 4);
        if(userInfoList.isEmpty()) {
            return map;
        }
        HashMap<Integer, String> userInfo = userInfoList.get(0);
        map.put("card_number", userInfo.get(1));
        map.put("card_expiry_date", userInfo.get(2));
        map.put("card_cvv", userInfo.get(3));
        map.put("cardholder_name", userInfo.get(4));

        return map;
    }

    public String getUserId() {
        List<HashMap<Integer, String>> userIdList = ConnectionUtil.getMultipleColumns("SELECT id FROM users WHERE user_credentials_id IN (SELECT id FROM user_credentials WHERE username = '" + getLoggedInUserName() + "')", 1);
        return userIdList.get(0).get(1);
    }

    public void createUser(String username, String password, String name, String birthday, String address_detail, String city, String country) {
        String user_credentials_id = UUID.randomUUID().toString();
        String cards_id = UUID.randomUUID().toString();
        ConnectionUtil.parseUpdateQuery("INSERT INTO user_credentials (id, username, password) VALUES " +
                "('" + user_credentials_id + "', '" + username + "', '" + passwordEncoder().encode(password) + "')");
        ConnectionUtil.parseUpdateQuery("INSERT INTO users (id, name, birthday, address_detail, city, country, cards_id, user_credentials_id) VALUES " +
                "('" + UUID.randomUUID().toString() + "', '" + name + "', '" + birthday + "', '" + address_detail + "', '" + city + "', '" + country + "', '" + cards_id + "', '" + user_credentials_id + "')");
        ConnectionUtil.parseUpdateQuery("INSERT INTO cards (id, card_number, card_expiry_date, card_cvv, cardholder_name) VALUES " +
                "('" + cards_id + "', '', '', '', '')");
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("USER"));
        inMemoryUserDetailsManager.createUser(
                new User(username, passwordEncoder().encode(password), list)
        );
    }

    public void updateCard(String card_number, String cardholder_name, String card_expiry_date, String card_cvv) {
        ConnectionUtil.parseUpdateQuery("UPDATE cards SET card_number = '" + card_number + "', card_expiry_date = '" + card_expiry_date + "', card_cvv = '" + card_cvv + "', cardholder_name = '" + cardholder_name + "' WHERE id in (SELECT cards_id FROM users WHERE id = '" + getUserId() + "')");
    }

    public void updateUserinfo(String name, String birthday, String address_detail, String city, String country) {
        ConnectionUtil.parseUpdateQuery("UPDATE users SET name = '" + name + "', birthday = '" + birthday + "', address_detail = '" + address_detail + "', city = '" + city +  "', country = '" + country + "' WHERE id ='" + getUserId() + "'");
    }

    public void updatePassword(String oldPassword, String newPassword) {
        ConnectionUtil.parseUpdateQuery("UPDATE user_credentials SET password = '" + passwordEncoder().encode(newPassword) +
                "' WHERE username = '" + getLoggedInUserName() + "'");
        inMemoryUserDetailsManager.changePassword(oldPassword, passwordEncoder().encode(newPassword));
    }

    public boolean isUsernameTaken(String username) {
        try {
            inMemoryUserDetailsManager.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean isPasswordCorrect(String password) {
        String oldPassword = inMemoryUserDetailsManager.loadUserByUsername(getLoggedInUserName()).getPassword();
        return passwordEncoder().matches(password, oldPassword);
    }
}
