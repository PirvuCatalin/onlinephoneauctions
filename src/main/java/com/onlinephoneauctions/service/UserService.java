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

import java.util.*;

import static com.onlinephoneauctions.security.WebSecurityConfig.passwordEncoder;

@Service
public class UserService {

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    /**
     * This method is used for returning the username of the current logged in user, using the in-memory context of Spring Security.
     *
     * @return the username of the current logged in user
     */
    public String getLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    /**
     * This method uses the in-memory context of Spring Security to check if the current user has the "ADMIN" role.
     *
     * @return boolean stating wether the current logged in user is an admin
     */
    public boolean isUserAdmin() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            for (GrantedAuthority authority : ((UserDetails) principal).getAuthorities()) {
                if (authority.getAuthority().equals("ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method is used for getting personal details of the logged in user from the database.
     *
     * @return {@link HashMap} containing the name, birthday, address details, city and country of the logged in user
     */
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

    /**
     * This method is used for getting card details of the logged in user from the database.
     *
     * @return {@link HashMap} containing HIGH RISK DECRYPTED information of the current logged in user: card number,
     * card expiry date, card cvv/cvc and cardholder's name
     */
    public Map<String, String> getUserCardInfo() {
        //todo implement encrypt / decrypt mechanism for card info
        Map<String, String> map = new HashMap<>();

        List<HashMap<Integer, String>> userInfoList =
                ConnectionUtil.getMultipleColumns("SELECT card_number, card_expiry_date, card_cvv, cardholder_name FROM cards " +
                        "WHERE id IN (SELECT cards_id FROM users WHERE id = '" + getUserId() + "')", 4);
        if (userInfoList.isEmpty()) {
            return map;
        }
        HashMap<Integer, String> userInfo = userInfoList.get(0);
        map.put("card_number", userInfo.get(1));
        map.put("card_expiry_date", userInfo.get(2));
        map.put("card_cvv", userInfo.get(3));
        map.put("cardholder_name", userInfo.get(4));

        return map;
    }

    /**
     * @return {@link String} containing the id of the current logged in user from the database.
     */
    public String getUserId() {
        String query =
                "SELECT id FROM users WHERE user_credentials_id IN (SELECT id FROM user_credentials WHERE username = '" + getLoggedInUserName() + "')";
        List<HashMap<Integer, String>> userIdList =
                ConnectionUtil.getMultipleColumns(query, 1);
        return userIdList.get(0).get(1);
    }

    /**
     * This method is used for creating a new user.
     * It does NOT contain validations, so before calling this, validate the user's input as it directly inserts into database.
     * This method also adds the "USER" role for the newly created user and adds it to the in-memory context of Spring Security.
     */
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

    /**
     * This method is used for updating card info of the current logged in user.
     * It does NOT contain validations, so before calling this, validate the user's input as it directly inserts into database.
     */
    public void updateCard(String card_number, String cardholder_name, String card_expiry_date, String card_cvv) {
        ConnectionUtil.parseUpdateQuery("UPDATE cards SET card_number = '" + card_number + "', card_expiry_date = '" + card_expiry_date + "', card_cvv = '" + card_cvv + "', cardholder_name = '" + cardholder_name + "' WHERE id in (SELECT cards_id FROM users WHERE id = '" + getUserId() + "')");
    }

    /**
     * This method is used for updating personal info of the current logged in user.
     * It does NOT contain validations, so before calling this, validate the user's input as it directly inserts into database.
     */
    public void updateUserinfo(String name, String birthday, String address_detail, String city, String country) {
        ConnectionUtil.parseUpdateQuery("UPDATE users SET name = '" + name + "', birthday = '" + birthday + "', address_detail = '" + address_detail + "', city = '" + city + "', country = '" + country + "' WHERE id ='" + getUserId() + "'");
    }

    /**
     * This method is used for updating the password the current logged in user.
     * It does NOT contain validations, so before calling this, validate the user's input as it directly inserts into database.
     * This method changes the password of the user at database level, buy also at the in-memory user management of Spring Security.
     */
    public void updatePassword(String oldPassword, String newPassword) {
        ConnectionUtil.parseUpdateQuery("UPDATE user_credentials SET password = '" + passwordEncoder().encode(newPassword) +
                "' WHERE username = '" + getLoggedInUserName() + "'");
        inMemoryUserDetailsManager.changePassword(oldPassword, passwordEncoder().encode(newPassword));
    }

    /**
     * This method uses the strategy of Spring Security of checking whether or not the username is already taken.
     *
     * @param username the username to check
     * @return boolean stating whether or not the username already exists
     */
    public boolean isUsernameTaken(String username) {
        try {
            inMemoryUserDetailsManager.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * @param password the password to check for match
     * @return {@link boolean} stating whether or not the password is inserted correctly
     */
    public boolean isPasswordCorrect(String password) {
        String oldPassword = inMemoryUserDetailsManager.loadUserByUsername(getLoggedInUserName()).getPassword();
        return passwordEncoder().matches(password, oldPassword);
    }
}
