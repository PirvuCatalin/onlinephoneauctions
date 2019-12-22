package com.onlinephoneauctions.controller;

import com.onlinephoneauctions.dto.AuctionInfoDTO;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.onlinephoneauctions.controller.RegisterController.isValidFormat;

@Controller
public class AuctionController {
    @Autowired
    private AuctionService auctionService;

    @Autowired
    private UserService userService;

    /**
     * Endpoint used for retrieving the auction's of the website.
     * If the user is an admin, it shows all the auctions (active, ended and not-validate ones), else it shows only the active ones.
     */
    @GetMapping("/auctions")
    public String showAuctions(Model model) {
        model.addAttribute("user_id", userService.getUserId());
        model.addAttribute("auctions", auctionService.retrieveActiveAuctions());
        model.addAttribute("phonesAvailable", auctionService.retrieveAvailablePhonesWithoutBrand());

        if (userService.isUserAdmin()) {
            addPriceIntervals(model, auctionService.retrievePriceIntervalsForAdmin());
            model.addAttribute("inactiveAuctions", auctionService.retrieveInactiveAuctions());
            model.addAttribute("notValidatedAuctions", auctionService.retrieveNotValidatedAuctions());
        } else {
            addPriceIntervals(model, auctionService.retrievePriceIntervals());
        }
        return "/auctions";
    }

    /**
     * Helper method used to show the correct price interval on the "/auctions" endpoint, based on the current filter on the website.
     */
    private void addPriceIntervals(Model model, String[] prices) {
        model.addAttribute("targetPriceMin", prices[0]);
        model.addAttribute("targetPriceMax", prices[1]);
        model.addAttribute("startingPriceMin", prices[2]);
        model.addAttribute("startingPriceMax", prices[3]);
        model.addAttribute("currentPriceMin", prices[4]);
        model.addAttribute("currentPriceMax", prices[5]);
    }

    /**
     * Endpoint used to filter the auctions in the website.
     */
    @PostMapping("/auctions")
    public String filterAuctions(Model model,
                                 @RequestParam(value = "phones_in_auction", required = false) String phones_in_auction,
                                 @RequestParam(value = "startingPrice", required = false) String startingPrice,
                                 @RequestParam(value = "currentPrice", required = false) String currentPrice,
                                 @RequestParam(value = "targetPrice", required = false) String targetPrice,
                                 @RequestParam(value = "sellerName", required = false) String sellerName) {
        String[] phones_in_auction_array = null;
        if (phones_in_auction != null) {
            phones_in_auction_array = phones_in_auction.split(",");
        }
        if (!(phones_in_auction == null || phones_in_auction.isEmpty())) {
            model.addAttribute("filtered", "true");
        }
        List<AvailablePhonesDTO> phonesAvailable = auctionService.retrieveAvailablePhonesWithoutBrand();
        String[] finalPhones_in_auction_array = phones_in_auction_array;
        if (finalPhones_in_auction_array != null && finalPhones_in_auction_array.length != 0) {
            phonesAvailable.forEach(phone -> {
                for (String s : finalPhones_in_auction_array) {
                    if (s.equals(phone.getPhoneName())) {
                        phone.setSelected(true);
                    }
                }
            });
        }
        if (startingPrice != null && !startingPrice.isEmpty()) {
            model.addAttribute("filtered", "true");
            String[] startingPriceSplit = startingPrice.split(",");
            model.addAttribute("startingPriceLow", startingPriceSplit[0]);
            model.addAttribute("startingPriceHigh", startingPriceSplit[1]);
        }
        if (currentPrice != null && !currentPrice.isEmpty()) {
            model.addAttribute("filtered", "true");
            String[] currentPriceSplit = currentPrice.split(",");
            model.addAttribute("currentPriceLow", currentPriceSplit[0]);
            model.addAttribute("currentPriceHigh", currentPriceSplit[1]);
        }
        if (targetPrice != null && !targetPrice.isEmpty()) {
            model.addAttribute("filtered", "true");
            String[] targetPriceSplit = targetPrice.split(",");
            model.addAttribute("targetPriceLow", targetPriceSplit[0]);
            model.addAttribute("targetPriceHigh", targetPriceSplit[1]);
        }
        if (sellerName != null && !sellerName.isEmpty()) {
            model.addAttribute("filtered", "true");
            model.addAttribute("sellerName", sellerName);
        }

        model.addAttribute("user_id", userService.getUserId());
        model.addAttribute("auctions", auctionService.retrieveActiveAuctions(phones_in_auction_array, startingPrice, currentPrice, targetPrice, sellerName));
        model.addAttribute("phonesAvailable", phonesAvailable);
        if (userService.isUserAdmin()) {
            addPriceIntervals(model, auctionService.retrievePriceIntervalsForAdmin());
            model.addAttribute("inactiveAuctions", auctionService.retrieveInactiveAuctions(phones_in_auction_array, startingPrice, currentPrice, targetPrice, sellerName));
            model.addAttribute("notValidatedAuctions", auctionService.retrieveNotValidatedAuctions(phones_in_auction_array, startingPrice, currentPrice, targetPrice, sellerName));
        } else {
            addPriceIntervals(model, auctionService.retrievePriceIntervals());
        }
        return "/auctions";
    }

    /**
     * Endpoint used to show the current user's auctions (active, ended and not-validated ones).
     * If an admin accesses this, it will redirect to "/error".
     */
    @GetMapping("/my-auctions")
    public String showMyAuctions(Model model) {
        if (userService.isUserAdmin()) {
            return "redirect:/error";
        }
        String userId = userService.getUserId();
        model.addAttribute("auctions", auctionService.retrieveMyActiveAuctions(userId));
        model.addAttribute("inactiveAuctions", auctionService.retrieveMyInactiveAuctions(userId));
        model.addAttribute("notValidatedAuctions", auctionService.retrieveMyNotValidatedAuctions(userId));
        return "/my-auctions";
    }

    /**
     * Endpoint used to delete an auction.
     * The user needs to have access to that auction or be an admin.
     */
    @GetMapping("/auctions/delete")
    public String deleteAuction(@RequestParam String id,
                                @RequestParam(value = "returnToMyAuctions", required = false) Boolean returnToMyAuctions,
                                Model model) {
        //model.addAttribute("user_id", userService.getUserId());
        //model.addAttribute("auctions", auctionService.retrieveActiveAuctions());
        if (auctionService.userHasAccessToAuction(id) || userService.isUserAdmin()) {
            auctionService.deleteAuction(id);
            model.addAttribute("errorMessage", "Auction successfully deleted!");
        } else {
            return "redirect:/error";
        }

        if (returnToMyAuctions != null) {
            return "redirect:/my-auctions";
        } else {
            return "redirect:/auctions";
        }
    }

    /**
     * Endpoint used by an admin to validate a newly created auction.
     */
    @GetMapping("/auctions/validate")
    public String validateAuction(@RequestParam String id, Model model) {
        if (userService.isUserAdmin()) {
            auctionService.validateAuction(id);
            model.addAttribute("errorMessage", "Auction successfully validated!");
        } else {
            return "redirect:/error";
        }

        return "redirect:/auctions";
    }

    /**
     * Endpoint used to render the page for creating a new auction / edit an existing one.
     */
    @GetMapping("/auctions-add")
    public String addAuction(@RequestParam(value = "id", required = false) String id,
                             @RequestParam(value = "returnToMyAuctions", required = false) Boolean returnToMyAuctions,
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

        if (returnToMyAuctions != null) {
            model.addAttribute("returnToMyAuctions", "true");
        }

        return "/auctions-add";
    }

    /**
     * Endpoint used to add a new auction or edit an existing one.
     * This validates the fields for standard errors.
     * It can also be used to create a new phone.
     */
    @PostMapping("/auctions-add")
    public String addAuction(@RequestParam(value = "id", required = false) String id,
                             @RequestParam(value = "returnToMyAuctions", required = false) Boolean returnToMyAuctions,
                             @RequestParam(value = "phoneAdd", required = false) String phoneAdd,
                             @RequestParam(value = "phone_brand", required = false) String phone_brand,
                             @RequestParam(value = "phone_model", required = false) String phone_model,
                             @RequestParam(value = "phones_in_auction", required = false) String phones_in_auction,
                             @RequestParam(value = "datetime_end", required = false) String datetime_end,
                             @RequestParam(value = "additional_info", required = false) String additional_info,
                             @RequestParam(value = "starting_price", required = false) String starting_price,
                             @RequestParam(value = "target_price", required = false) String target_price,
                             final Model model) {
        if (phoneAdd != null) {
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

        if (phoneAdd != null) {
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

            if (returnToMyAuctions != null) {
                return "redirect:/my-auctions";
            } else {
                return "redirect:/auctions";
            }
        }
    }

    /**
     * Endpoint used to render the auction's page.
     *
     * @param manuallyDo if present, this means to manually complete an ended auction
     * @param bidAmount  if present, this means that the user requested to bid for the current auction
     * @param id         the auction id from auction_info table
     */
    @GetMapping("/auction")
    public String showAuction(@RequestParam String id,
                              @RequestParam(required = false) String manuallyDo,
                              @RequestParam(required = false) String bidAmount,
                              Model model) {
        if (StringUtils.isEmpty(id)) {
            return "/error";
        }
        if (!StringUtils.isEmpty(manuallyDo) && manuallyDo.equals("true")) {
            auctionService.manuallyDoAuction(id);
        }
        if (!StringUtils.isEmpty(bidAmount)) {
            String result = auctionService.bid(id, bidAmount);
            if (!result.equals("OK")) {
                model.addAttribute("errorMessage", result);
            }
        }

        AuctionInfoDTO auctionInfo = auctionService.retrieveAuctionInfo(id);
        model.addAttribute("auction", auctionInfo);
        model.addAttribute("bids", auctionService.retrieveBidsForAuction(id));
        DateTimeFormatter dbDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime auctionDateTimeEnd = LocalDateTime.parse(auctionInfo.getDatetime_end(), dbDtf);
        if (auctionDateTimeEnd.isBefore(now)) {
            model.addAttribute("auctionEnded", "yes");
        }
        if (auctionService.canBeManuallyDone(auctionInfo)) {
            model.addAttribute("canBeManuallyDone", "yes");
        }
        if (auctionService.canBid(auctionInfo)) {
            model.addAttribute("canBid", "yes");
        }
        return "auction";
    }

    /**
     * Endpoint used to retrieve the current logged in user bids.
     * If the user is an admin, it redirects to "/error".
     */
    @GetMapping("/my-bids")
    public String showMyBids(Model model) {
        if (userService.isUserAdmin()) {
            return "redirect:/error";
        }
        String userId = userService.getUserId();
        model.addAttribute("auctions", auctionService.retrieveMyBidsByAuction(userId));
        return "/my-bids";
    }

    /**
     * Helper method used to add attributes to frontend.
     */
    private void addAttributes(Model model, String datetime_end, String starting_price,
                               String target_price, String additional_info) {
        model.addAttribute("datetime_end", datetime_end);
        model.addAttribute("starting_price", starting_price);
        model.addAttribute("target_price", target_price);
        model.addAttribute("additional_info", additional_info);
    }
}
