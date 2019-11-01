package com.onlinephoneauctions.controller;

import com.onlinephoneauctions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public RedirectView register(@RequestParam(value = "username", required = false) String username,
                                 @RequestParam(value = "password", required = false) String password,
                                 @RequestParam(value = "passwordConfirm", required = false) String passwordConfirm,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "birthday", required = false) String birthday,
                                 @RequestParam(value = "address_detail", required = false) String address_detail,
                                 @RequestParam(value = "city", required = false) String city,
                                 @RequestParam(value = "country", required = false) String country,
                                 final RedirectAttributes redirectAttributes) {
        if (username == null || username.length() < 5) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username must be at least 5 characters long!");
            addAttributes(redirectAttributes, username, name, birthday, address_detail, city, country);
            return new RedirectView("/register");
        }

        if (userService.isUsernameTaken(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username already exists!");
            addAttributes(redirectAttributes, username, name, birthday, address_detail, city, country);
            return new RedirectView("/register");
        }

        if (password == null || password.length() < 5) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 5 characters long!");
            addAttributes(redirectAttributes, username, name, birthday, address_detail, city, country);
            return new RedirectView("/register");
        }

        if (!password.equals(passwordConfirm)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords must match!");
            addAttributes(redirectAttributes, username, name, birthday, address_detail, city, country);
            return new RedirectView("/register");
        }

        if(StringUtils.isEmpty(name)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Name cannot be empty!");
            addAttributes(redirectAttributes, username, name, birthday, address_detail, city, country);
            return new RedirectView("/register");
        }

        if(StringUtils.isEmpty(birthday)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Birthday cannot be empty!");
            addAttributes(redirectAttributes, username, name, birthday, address_detail, city, country);
            return new RedirectView("/register");
        }

        if(!isValidFormat("yyyy-MM-dd", birthday)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Birthday format must be 'yyyy-MM-dd'!");
            addAttributes(redirectAttributes, username, name, birthday, address_detail, city, country);
            return new RedirectView("/register");
        }

        if(StringUtils.isEmpty(address_detail)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Address Detail cannot be empty!");
            addAttributes(redirectAttributes, username, name, birthday, address_detail, city, country);
            return new RedirectView("/register");
        }

        if(StringUtils.isEmpty(city)) {
            redirectAttributes.addFlashAttribute("errorMessage", "City cannot be empty!");
            addAttributes(redirectAttributes, username, name, birthday, address_detail, city, country);
            return new RedirectView("/register");
        }

        if(StringUtils.isEmpty(country)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Country cannot be empty!");
            addAttributes(redirectAttributes, username, name, birthday, address_detail, city, country);
            return new RedirectView("/register");
        }

        // credentials fine, create new user and redirect to login page
        userService.createUser(username, password, name, birthday, address_detail, city, country);

        redirectAttributes.addFlashAttribute("errorMessage", "Account successfully created! Please login.");
        return new RedirectView("/login");
    }

    private void addAttributes(RedirectAttributes redirectAttributes, String username, String name, String birthday,
                          String address_detail, String city, String country) {
        redirectAttributes.addFlashAttribute("username", username);
        redirectAttributes.addFlashAttribute("name", name);
        redirectAttributes.addFlashAttribute("birthday", birthday);
        redirectAttributes.addFlashAttribute("address_detail", address_detail);
        redirectAttributes.addFlashAttribute("city", city);
        redirectAttributes.addFlashAttribute("country", country);
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }
}
