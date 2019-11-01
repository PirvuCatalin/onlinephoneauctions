package com.onlinephoneauctions.controller;

import com.onlinephoneauctions.dto.AvailablePhonesDTO;
import com.onlinephoneauctions.service.AuctionService;
import com.onlinephoneauctions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.onlinephoneauctions.controller.RegisterController.isValidFormat;

@Controller
public class AuctionController {
    @Autowired
    private AuctionService auctionService;

    @Autowired
    private UserService userService;

    @GetMapping("/auctions")
    public String showAuctions(ModelMap model) {
        model.addAttribute("user_id", userService.getUserId());
        model.addAttribute("auctions", auctionService.retrieveActiveAuctions());
        return "/auctions";
    }

    @GetMapping("/auctions/delete")
    public String deleteAuction(@RequestParam String id,  ModelMap model) {
        //model.addAttribute("user_id", userService.getUserId());
        //model.addAttribute("auctions", auctionService.retrieveActiveAuctions());
        auctionService.deleteAuction(id);
        return "redirect:/auctions";
    }

    @GetMapping("/auctions-add")
    public String addAuction(@RequestParam(value = "id", required = false) String id,
                             final Model model) {
        if (id != null) {

        } else {
            List<AvailablePhonesDTO> phonesAvailable = auctionService.retrieveAvailablePhones();
            model.addAttribute("phonesAvailable", phonesAvailable);
        }

        return "/auctions-add";
    }

    @PostMapping("/auctions-add")
    public String addAuction(@RequestParam(value = "id", required = false) String id,
                                   @RequestParam(value = "phones_in_auction", required = false) String phones_in_auction,
                                   @RequestParam(value = "datetime_end", required = false) String datetime_end,
                                   @RequestParam(value = "additional_info", required = false) String additional_info,
                                   @RequestParam(value = "starting_price", required = false) String starting_price,
                                   @RequestParam(value = "target_price", required = false) String target_price,
                                   final Model model) {
        String[] phones_in_auction_array = null;
        if(phones_in_auction != null) {
            phones_in_auction_array = phones_in_auction.split(",");
        }

        if(id != null) {

        } else {
            List<AvailablePhonesDTO> phonesAvailable = auctionService.retrieveAvailablePhones();
            String[] finalPhones_in_auction_array = phones_in_auction_array;
            if(finalPhones_in_auction_array != null && finalPhones_in_auction_array.length != 0) {
                phonesAvailable.forEach(phone -> {
                    for (String s : finalPhones_in_auction_array) {
                        if(s.equals(phone.getPhoneId())) {
                            phone.setSelected(true);
                        }
                    }
                });
            }
            model.addAttribute("phonesAvailable", phonesAvailable);
        }

        if(StringUtils.isEmpty(phones_in_auction)) {
            model.addAttribute("errorMessage", "You should add at least a phone to the auction!");
            addAttributes(model, datetime_end, starting_price, target_price, additional_info);
            return "/auctions-add";
        }

        if(StringUtils.isEmpty(datetime_end)) {
            model.addAttribute("errorMessage", "Auction end datetime cannot be empty!");
            addAttributes(model, datetime_end, starting_price, target_price, additional_info);
            return "/auctions-add";
        }

        if(!isValidFormat("dd-MM-yyyy HH:mm:ss", datetime_end)) {
            model.addAttribute("errorMessage", "Auction end datetime format must be 'dd-MM-yyyy HH:mm:ss'!");
            addAttributes(model, datetime_end, starting_price, target_price, additional_info);
            return "/auctions-add";
        }

        if(StringUtils.isEmpty(starting_price) || !starting_price.matches("[0-9]||[0-9]+.+[0-9]")) {
            model.addAttribute("errorMessage", "Starting price must be composed only of digits (e.g. '2', '100.3', etc.)!");
            addAttributes(model, datetime_end, starting_price, target_price, additional_info);
            return "/auctions-add";
        }

        if(StringUtils.isEmpty(target_price) || !target_price.matches("[0-9]||[0-9]+.+[0-9]")) {
            model.addAttribute("errorMessage", "Target price must be composed only of digits (e.g. '2', '100.3', etc.)!");
            addAttributes(model, datetime_end, starting_price, target_price, additional_info);
            return "/auctions-add";
        }

        // create new auction
        auctionService.createAuction(phones_in_auction, datetime_end, additional_info, starting_price, target_price);

        model.addAttribute("errorMessage", "Auction successfully created!");

        return "/auctions";
    }

    private void addAttributes(Model model, String datetime_end, String starting_price,
                               String target_price, String additional_info) {
        model.addAttribute("datetime_end", datetime_end);
        model.addAttribute("starting_price", starting_price);
        model.addAttribute("target_price", target_price);
        model.addAttribute("additional_info", additional_info);
    }
}
