package com.onlinephoneauctions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    /**
     * Endpoint used for login that returns specific messages if it was successful or not,
     * but also if it was a logout (using the logout parameter).
     */
    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Wrong username or password!");
        } else if (logout != null) {
            model.addAttribute("errorMessage", "You have successfully logged out.");
        }

        return "login";
    }
}
