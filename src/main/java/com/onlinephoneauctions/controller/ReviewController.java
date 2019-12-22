package com.onlinephoneauctions.controller;

import com.onlinephoneauctions.service.ReviewService;
import com.onlinephoneauctions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ReviewController {
    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    /**
     * This method is used for retrieving all the auctions reviews.
     *
     * @param sellerName the name of the seller to filter by (it can also be empty)
     */
    @GetMapping("/reviews")
    public String showReviews(@RequestParam(value = "sellerName", required = false) String sellerName,
                              Model model) {
        model.addAttribute("sellerName", sellerName);
        if (sellerName != null && sellerName.length() > 0) {
            String sellerNameTrimmed = StringUtils.trimLeadingWhitespace(sellerName);
            sellerNameTrimmed = StringUtils.trimTrailingWhitespace(sellerNameTrimmed);
            model.addAttribute("reviews", reviewService.retrieveReviewsFilteredBySellerName(sellerNameTrimmed));
        } else {
            model.addAttribute("reviews", reviewService.retrieveAllReviews());
        }
        return "/reviews";
    }

    /**
     * Endpoint used for deleting a review.
     * Only admins can delete reviews.
     */
    @GetMapping("/reviews/delete")
    public String deleteReview(@RequestParam String id,
                               @RequestParam(value = "sellerName", required = false) String sellerName) {
        if (userService.isUserAdmin()) {
            reviewService.deleteReview(id);
            return "redirect:/reviews";
        } else {
            return "redirect:/error";
        }
    }

    /**
     * This mapping adds a new review to the website.
     * There is not validation in the backend, but using the current logic, this can be accessed only after special
     * conditions are met (see {@link ReviewService#addReview(String, String, String, String)})
     */
    @GetMapping("/reviews/add")
    public String addReview(@RequestParam String auctionId,
                            @RequestParam String stars,
                            @RequestParam String review) {
//        if (auctionService.userHasWon(auctionId)) {
        reviewService.addReview(userService.getUserId(), auctionId, stars, review);
        return "redirect:/my-bids";
//        } else {
//            return "redirect:/error";
//        }
    }
}
