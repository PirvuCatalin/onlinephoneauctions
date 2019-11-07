package com.onlinephoneauctions.config;

import com.onlinephoneauctions.dbconnect.ConnectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Component
public class OnApplicationStartUp {
    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        addUsersToAuthenticationManager();
    }

    private void addUsersToAuthenticationManager() {
        List<HashMap<Integer, String>> credentials = ConnectionUtil.
                getMultipleColumns("SELECT username, password FROM user_credentials", 2);
        credentials.forEach(map -> {
            if(map.get(1).equals("admin")) {
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
}