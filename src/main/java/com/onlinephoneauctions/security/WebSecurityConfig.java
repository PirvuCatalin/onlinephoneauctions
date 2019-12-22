package com.onlinephoneauctions.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * This method returns the encoder used for password encoding.
     *
     * @return PasswordEncoder containing the encoder used for the in-memory user management of Spring Security
     */
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This method is used to configure the security of the web application.
     * It uses the in-memory management of Spring Security.
     */
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager()).passwordEncoder(passwordEncoder());
    }

    /**
     * This method can be configured to contain default users, without relying on the database's input as seen on the commented out line.
     *
     * @return InMemoryUserDetailsManager containing an empty in-memory user manager
     */
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        final Properties users = new Properties();
        //users.put("test", passwordEncoder().encode("test") + ",USER,enabled");
        return new InMemoryUserDetailsManager(users);
    }

    /**
     * This method is used to configure the authorization access for users on the specific mapping of the website.
     * All users (including anonymous) are allowed to access the login/register/h2(database) endpoints.
     * (for the database endpoint though it will require a database administrator password)
     * The rest of the mappings require "USER" role.
     * This method is also used for defining the endpoint of the login and
     * for disabling Cross-Site-Request-Forgery (DEVELOPMENT ONLY!).
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/", "/*todo*/**").hasAuthority("USER")
                .antMatchers("/", "/*account*/**").hasAuthority("USER")
                .antMatchers("/", "/*auctions*/**").hasAuthority("USER")
                .antMatchers("/", "/*my-auctions*/**").hasAuthority("USER")
                .antMatchers("/", "/*auctions-add*/**").hasAuthority("USER")
                .antMatchers("/", "/*reviews*/**").hasAuthority("USER")
                .antMatchers("/", "/*auction*/**").hasAuthority("USER")
                .antMatchers("/", "/*my-bids*/**").hasAuthority("USER")
                .and().formLogin().loginPage("/login").permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}