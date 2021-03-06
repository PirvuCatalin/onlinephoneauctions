package com.onlinephoneauctions.controller;

import com.onlinephoneauctions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController {
    @Autowired
    private UserService userService;

    /**
     * The welcome endpoint of the website, used to print the logged in username and to be redirected after successful login.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showWelcomePage(ModelMap model) {
        model.put("name", userService.getLoggedInUserName());
        return "welcome";
    }
}