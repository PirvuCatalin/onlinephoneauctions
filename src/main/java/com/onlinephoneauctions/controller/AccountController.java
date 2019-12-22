package com.onlinephoneauctions.controller;

import com.onlinephoneauctions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import static com.onlinephoneauctions.controller.RegisterController.isValidFormat;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    /**
     * This mapping is used for adding the personal info of the current logged in user to the frontend.
     * It will show only last digits of the user's card number and none of his card CVV/CVC.
     */
    @GetMapping("/account")
    public String account(final Model model) {
        model.addAttribute("username", userService.getLoggedInUserName());
        userService.getUserInfo().forEach((attribute, value) -> {
            model.addAttribute(attribute, value);
        });
        userService.getUserCardInfo().forEach((attribute, value) -> {
            model.addAttribute(attribute, value);
            if (attribute.equals("card_number") && value != null && value.length() == 16) {
                model.addAttribute(attribute, "XXXX XXXX XXXX " + value.substring(12));
            } else if (attribute.equals("card_cvv") && value != null && value.length() == 3) {
                model.addAttribute(attribute, "xxx");
            } else {
                model.addAttribute(attribute, value);
            }
        });
        return "account";
    }

    /**
     * This mapping is used for letting the user choose a new password. Can be accessed under "/account".
     * Also contains relevant validations.
     *
     * @param username        the username of the current logged in user
     * @param currentPassword the password of the current logged in user
     * @param password        the new password of the current logged in user
     * @param passwordConfirm the confirmation of the new password of the current logged in user
     */
    @PostMapping("/account/change-password")
    public RedirectView changePassword(@RequestParam(value = "username", required = false) String username,
                                       @RequestParam(value = "currentPassword", required = false) String currentPassword,
                                       @RequestParam(value = "password", required = false) String password,
                                       @RequestParam(value = "passwordConfirm", required = false) String passwordConfirm,
                                       final RedirectAttributes redirectAttributes) {

        if (!userService.isPasswordCorrect(currentPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "The current password you inserted is wrong!");
            return new RedirectView("/account");
        }

        if (password == null || password.length() < 5) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 5 characters long!");
            return new RedirectView("/account");
        }

        if (!password.equals(passwordConfirm)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords must match!");
            return new RedirectView("/account");
        }

        // credentials fine, update user password
        userService.updatePassword(currentPassword, password);

        redirectAttributes.addFlashAttribute("errorMessage", "Password successfully updated!");
        return new RedirectView("/account");
    }

    /**
     * This mapping is used for letting the user enter new card details. Can be accessed under "/account".
     * Also contains relevant validations.
     *
     * @param card_number      the card number
     * @param cardholder_name  the cardholder name
     * @param card_expiry_date the card expiry date
     * @param card_cvv         the card cvv/cvc
     */
    @PostMapping("/account/change-card")
    public RedirectView changeCard(@RequestParam(value = "card_number", required = false) String card_number,
                                   @RequestParam(value = "cardholder_name", required = false) String cardholder_name,
                                   @RequestParam(value = "card_expiry_date", required = false) String card_expiry_date,
                                   @RequestParam(value = "card_cvv", required = false) String card_cvv,
                                   final RedirectAttributes redirectAttributes) {

        if (card_number == null || card_number.length() != 16 || !card_number.matches("[0-9]+")) {
            redirectAttributes.addFlashAttribute("errorMessageCardInfo", "Card Number must be composed of 16 digits!");
            return new RedirectView("/account");
        }
        if (cardholder_name == null || cardholder_name.length() == 0) {
            redirectAttributes.addFlashAttribute("errorMessageCardInfo", "Cardholder Name cannot be empty!");
            return new RedirectView("/account");
        }
        if (card_expiry_date == null || card_expiry_date.length() == 0) {
            redirectAttributes.addFlashAttribute("errorMessageCardInfo", "Card Expiry Date cannot be empty!");
            return new RedirectView("/account");
        }
        if (!isValidFormat("MM/yy", card_expiry_date)) {
            redirectAttributes.addFlashAttribute("errorMessageCardInfo", "Card Expiry Date format must be 'MM/yy'!");
            return new RedirectView("/account");
        }
        if (card_cvv == null || card_cvv.length() != 3 || !card_number.matches("[0-9]+")) {
            redirectAttributes.addFlashAttribute("errorMessageCardInfo", "Card CVV must be composed of 3 digits!");
            return new RedirectView("/account");
        }
        // inserted data is fine, update card
        userService.updateCard(card_number, cardholder_name, card_expiry_date, card_cvv);

        redirectAttributes.addFlashAttribute("errorMessageCardInfo", "Card successfully updated!");
        return new RedirectView("/account");
    }

    /**
     * This mapping is used for letting the user edit his account info. Can be accessed under "/account".
     * The username cannot be changed!
     * Also contains relevant validations.
     */
    @PostMapping("/account/change-userinfo")
    public RedirectView changeUserinfo(@RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "birthday", required = false) String birthday,
                                       @RequestParam(value = "address_detail", required = false) String address_detail,
                                       @RequestParam(value = "city", required = false) String city,
                                       @RequestParam(value = "country", required = false) String country,
                                       final RedirectAttributes redirectAttributes) {

        if (StringUtils.isEmpty(name)) {
            redirectAttributes.addFlashAttribute("errorMessageUserInfo", "Name cannot be empty!");
            return new RedirectView("/account");
        }

        if (StringUtils.isEmpty(birthday)) {
            redirectAttributes.addFlashAttribute("errorMessageUserInfo", "Birthday cannot be empty!");
            return new RedirectView("/account");
        }

        if (!isValidFormat("yyyy-MM-dd", birthday)) {
            redirectAttributes.addFlashAttribute("errorMessageUserInfo", "Birthday format must be 'yyyy-MM-dd'!");
            return new RedirectView("/account");
        }

        if (StringUtils.isEmpty(address_detail)) {
            redirectAttributes.addFlashAttribute("errorMessageUserInfo", "Address Detail cannot be empty!");
            return new RedirectView("/account");
        }

        if (StringUtils.isEmpty(city)) {
            redirectAttributes.addFlashAttribute("errorMessageUserInfo", "City cannot be empty!");
            return new RedirectView("/account");
        }

        if (StringUtils.isEmpty(country)) {
            redirectAttributes.addFlashAttribute("errorMessageUserInfo", "Country cannot be empty!");
            return new RedirectView("/account");
        }

        // credentials fine, update user password
        userService.updateUserinfo(name, birthday, address_detail, city, country);

        redirectAttributes.addFlashAttribute("errorMessageUserInfo", "User Info successfully updated!");
        return new RedirectView("/account");
    }
}
