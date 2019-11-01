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
            inMemoryUserDetailsManager.createUser(
                    new User(map.get(1), map.get(2), com.sun.tools.javac.util.List.of(new SimpleGrantedAuthority("USER")))
            );
        });
    }
}