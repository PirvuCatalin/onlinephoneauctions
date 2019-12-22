package com.onlinephoneauctions.service;

import com.onlinephoneauctions.dbconnect.ConnectionUtil;
import com.onlinephoneauctions.dto.ReviewsDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    /**
     * @return {@link ArrayList<ReviewsDTO>} containing all the reviews for the ended auctions of the website
     */
    public List<ReviewsDTO> retrieveAllReviews() {
        List<ReviewsDTO> reviews = new ArrayList<>();
        List<HashMap<Integer, String>> reviewsMap = ConnectionUtil.getMultipleColumns(
                "SELECT r.id, " +
                        "seller.name as seller, " +
                        "phone_names.phone_names as auction, " +
                        "buyer.name as buyer, " +
                        "ai.datetime_end as auctionEndDatetime, " +
                        "ai.current_price_bidded as buyPrice, " +
                        "r.stars, " +
                        "r.review " +
                        "FROM reviews r " +
                        "INNER JOIN auction_info ai " +
                        "ON r.auction_info_id = ai.id " +
                        "INNER JOIN users seller " +
                        "ON seller.id = ai.seller_id " +
                        "INNER JOIN users buyer " +
                        "ON buyer.id = r.reviewer_id " +
                        "INNER JOIN ( " +
                        "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                        "FROM phones p, phones_in_auction pa " +
                        "WHERE p.id = pa.phone_id " +
                        "GROUP BY pa.auction_info_id " +
                        ") phone_names " +
                        "ON phone_names.auction_info_id = ai.id",
                8);
        return getReviewsDTOS(reviews, reviewsMap);
    }

    /**
     * @param sellerName the name of the seller to filter by
     * @return {@link ArrayList<ReviewsDTO>} containing all the reviews for the ended auctions of the website,
     * filtered by seller's name
     */
    public List<ReviewsDTO> retrieveReviewsFilteredBySellerName(String sellerName) {
        List<ReviewsDTO> reviews = new ArrayList<>();
        List<HashMap<Integer, String>> reviewsMap = ConnectionUtil.getMultipleColumns(
                "SELECT r.id, " +
                        "seller.name as seller, " +
                        "phone_names.phone_names as auction, " +
                        "buyer.name as buyer, " +
                        "ai.datetime_end as auctionEndDatetime, " +
                        "ai.current_price_bidded as buyPrice, " +
                        "r.stars, " +
                        "r.review " +
                        "FROM reviews r " +
                        "INNER JOIN auction_info ai " +
                        "ON r.auction_info_id = ai.id " +
                        "INNER JOIN users seller " +
                        "ON seller.id = ai.seller_id " +
                        "INNER JOIN users buyer " +
                        "ON buyer.id = r.reviewer_id " +
                        "INNER JOIN ( " +
                        "SELECT group_concat(p.phone_model SEPARATOR ' + ') as phone_names, auction_info_id " +
                        "FROM phones p, phones_in_auction pa " +
                        "WHERE p.id = pa.phone_id " +
                        "GROUP BY pa.auction_info_id " +
                        ") phone_names " +
                        "ON phone_names.auction_info_id = ai.id " +
                        "WHERE lower(seller.name) like '%" + sellerName.toLowerCase() + "%'",
                8);
        return getReviewsDTOS(reviews, reviewsMap);
    }

    /**
     * Helper method to convert between data from database and objects.
     */
    private List<ReviewsDTO> getReviewsDTOS(List<ReviewsDTO> reviews, List<HashMap<Integer, String>> reviewsMap) {
        reviewsMap.forEach(map -> {
            ReviewsDTO reviewsDTO = new ReviewsDTO();
            reviewsDTO.setId(map.get(1));
            reviewsDTO.setSeller(map.get(2));
            reviewsDTO.setAuction(map.get(3));
            reviewsDTO.setBuyer(map.get(4));
            reviewsDTO.setAuctionEndDatetime(map.get(5));
            reviewsDTO.setBuyPrice(Double.parseDouble(map.get(6)));
            reviewsDTO.setStars(Integer.parseInt(map.get(7)));
            reviewsDTO.setReview(map.get(8));
            reviews.add(reviewsDTO);
        });
        return reviews;
    }

    /**
     * @param id the id of the review that will be deleted
     */
    public void deleteReview(String id) {
        ConnectionUtil.parseUpdateQuery("DELETE FROM reviews WHERE id = '" + id + "';");
    }

    /**
     * This method adds a new review to the website.
     * The review logic as of current version states that only the user that have successfully won the auction is
     * allowed to review (and only once). This have to be checked before calling this method.
     */
    public void addReview(String reviewerId, String auctionId, String stars, String review) {
        String query =
                "INSERT INTO REVIEWS (id, reviewer_id, auction_info_id, stars, review) " +
                        "VALUES ('" + UUID.randomUUID().toString() + "', '" + reviewerId + "', '" + auctionId + "', '" + stars + "', '" + review + "');";
        ConnectionUtil.parseUpdateQuery(query);
    }
}
