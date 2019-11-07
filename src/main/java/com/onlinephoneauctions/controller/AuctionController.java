package com.onlinephoneauctions.controller;

import com.onlinephoneauctions.dto.AvailablePhonesDTO;
import com.onlinephoneauctions.service.AuctionService;
import com.onlinephoneauctions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.onlinephoneauctions.controller.RegisterController.isValidFormat;

@Controller
public class AuctionController {
    @Autowired
    private AuctionService auctionService;

    @Autowired
    private UserService userService;

    @GetMapping("/auctions")
    public String showAuctions(Model model) {
        model.addAttribute("user_id", userService.getUserId());
        model.addAttribute("auctions", auctionService.retrieveActiveAuctions());
        if(userService.isUserAdmin()) {
            model.addAttribute("inactiveAuctions", auctionService.retrieveInactiveAuctions());
            model.addAttribute("notValidatedAuctions", auctionService.retrieveNotValidatedAuctions());
        }
        return "/auctions";
    }

    @GetMapping("/auctions/delete")
    public String deleteAuction(@RequestParam String id, Model model) {
        //model.addAttribute("user_id", userService.getUserId());
        //model.addAttribute("auctions", auctionService.retrieveActiveAuctions());
        if (auctionService.userHasAccessToAuction(id)) {
            auctionService.deleteAuction(id);
            model.addAttribute("errorMessage", "Auction successfully deleted!");
        } else {
            return "redirect:/error";
        }
        return "redirect:/auctions";
    }

    @GetMapping("/auctions/validate")
    public String validateAuction(@RequestParam String id, Model model) {
        if(userService.isUserAdmin()) {
            auctionService.validateAuction(id);
            model.addAttribute("errorMessage", "Auction successfully validated!");
        } else {
            return "redirect:/error";
        }

        return "redirect:/auctions";
    }

    @GetMapping("/auctions-add")
    public String addAuction(@RequestParam(value = "id", required = false) String id,
                             final Model model) {
        if (id != null) {
            //edit already existing auction
            if (auctionService.userHasAccessToAuction(id)) {
                auctionService.getAuctionInfo(id).forEach((attribute, value) -> {
                    model.addAttribute(attribute, value);
                });
                List<AvailablePhonesDTO> phonesAvailable = auctionService.retrieveAvailablePhonesAndPhonesInAuction(id);
                model.addAttribute("phonesAvailable", phonesAvailable);
            } else {
                return "redirect:/error";
            }

        } else {
            List<AvailablePhonesDTO> phonesAvailable = auctionService.retrieveAvailablePhones();
            model.addAttribute("phonesAvailable", phonesAvailable);
        }

        return "/auctions-add";
    }

    @PostMapping("/auctions-add")
    public String addAuction(@RequestParam(value = "id", required = false) String id,
                             @RequestParam(value = "phoneAdd", required = false) String phoneAdd,
                             @RequestParam(value = "phone_brand", required = false) String phone_brand,
                             @RequestParam(value = "phone_model", required = false) String phone_model,
                             @RequestParam(value = "phones_in_auction", required = false) String phones_in_auction,
                             @RequestParam(value = "datetime_end", required = false) String datetime_end,
                             @RequestParam(value = "additional_info", required = false) String additional_info,
                             @RequestParam(value = "starting_price", required = false) String starting_price,
                             @RequestParam(value = "target_price", required = false) String target_price,
                             final Model model) {
        if(phoneAdd != null) {
            auctionService.createPhone(phone_brand, phone_model);
        }

        String[] phones_in_auction_array = null;
        if (phones_in_auction != null) {
            phones_in_auction_array = phones_in_auction.split(",");
        }
        List<AvailablePhonesDTO> phonesAvailable = auctionService.retrieveAvailablePhones();
        String[] finalPhones_in_auction_array = phones_in_auction_array;
        if (finalPhones_in_auction_array != null && finalPhones_in_auction_array.length != 0) {
            phonesAvailable.forEach(phone -> {
                for (String s : finalPhones_in_auction_array) {
                    if (s.equals(phone.getPhoneId())) {
                        phone.setSelected(true);
                    }
                }
            });
        }
        model.addAttribute("phonesAvailable", phonesAvailable);

        if(phoneAdd != null) {
            addAttributes(model, datetime_end, starting_price, target_price, additional_info);
            return "/auctions-add";
        } else {
            if (StringUtils.isEmpty(phones_in_auction)) {
                model.addAttribute("errorMessage", "You should add at least a phone to the auction!");
                addAttributes(model, datetime_end, starting_price, target_price, additional_info);
                return "/auctions-add";
            }

            if (StringUtils.isEmpty(datetime_end)) {
                model.addAttribute("errorMessage", "Auction end datetime cannot be empty!");
                addAttributes(model, datetime_end, starting_price, target_price, additional_info);
                return "/auctions-add";
            }

            if (!isValidFormat("dd-MM-yyyy HH:mm:ss", datetime_end)) {
                model.addAttribute("errorMessage", "Auction end datetime format must be 'dd-MM-yyyy HH:mm:ss'!");
                addAttributes(model, datetime_end, starting_price, target_price, additional_info);
                return "/auctions-add";
            }

            if (StringUtils.isEmpty(starting_price) || !starting_price.matches("\\d+\\.?(\\d+)?")) {
                model.addAttribute("errorMessage", "Starting price must be composed only of digits (e.g. '2', '100.3', etc.)!");
                addAttributes(model, datetime_end, starting_price, target_price, additional_info);
                return "/auctions-add";
            }

            if (StringUtils.isEmpty(target_price) || !target_price.matches("\\d+\\.?(\\d+)?")) {
                model.addAttribute("errorMessage", "Target price must be composed only of digits (e.g. '2', '100.3', etc.)!");
                addAttributes(model, datetime_end, starting_price, target_price, additional_info);
                return "/auctions-add";
            }
            if (id == null) {
                // create new auction
                auctionService.createAuction(phones_in_auction, datetime_end, additional_info, starting_price, target_price);
                //model.addAttribute("errorMessage", "Auction successfully created!");
            } else {
                // update existing auction
                if (auctionService.userHasAccessToAuction(id)) {
                    auctionService.updateAuction(id, phones_in_auction, datetime_end, additional_info, starting_price, target_price);
                    //model.addAttribute("errorMessage", "Auction successfully updated!");
                } else {
                    return "redirect:/error";
                }
            }
            return "redirect:/auctions";
        }
    }

    private void addAttributes(Model model, String datetime_end, String starting_price,
                               String target_price, String additional_info) {
        model.addAttribute("datetime_end", datetime_end);
        model.addAttribute("starting_price", starting_price);
        model.addAttribute("target_price", target_price);
        model.addAttribute("additional_info", additional_info);
    }
}
