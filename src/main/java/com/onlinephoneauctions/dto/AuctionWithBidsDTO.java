package com.onlinephoneauctions.dto;

import java.util.List;

public class AuctionWithBidsDTO {
    private String auction_title;
    private String auction_datetime_end;
    private double auction_current_price_bidded;
    private String auction_seller;
    private String auction_info_id;
    private boolean is_successfully_done;
    private boolean canBeReviewed;
    private List<BidDTO> bids;

    public AuctionWithBidsDTO() {
    }

    public boolean isCanBeReviewed() {
        return canBeReviewed;
    }

    public void setCanBeReviewed(boolean canBeReviewed) {
        this.canBeReviewed = canBeReviewed;
    }

    public List<BidDTO> getBids() {
        return bids;
    }

    public void setBids(List<BidDTO> bids) {
        this.bids = bids;
    }

    public double getAuction_current_price_bidded() {
        return auction_current_price_bidded;
    }

    public void setAuction_current_price_bidded(double auction_current_price_bidded) {
        this.auction_current_price_bidded = auction_current_price_bidded;
    }

    public String getAuction_datetime_end() {
        return auction_datetime_end;
    }

    public void setAuction_datetime_end(String auction_datetime_end) {
        this.auction_datetime_end = auction_datetime_end;
    }

    public String getAuction_title() {
        return auction_title;
    }

    public void setAuction_title(String auction_title) {
        this.auction_title = auction_title;
    }

    public String getAuction_seller() {
        return auction_seller;
    }

    public void setAuction_seller(String auction_seller) {
        this.auction_seller = auction_seller;
    }

    public boolean isIs_successfully_done() {
        return is_successfully_done;
    }

    public void setIs_successfully_done(boolean is_successfully_done) {
        this.is_successfully_done = is_successfully_done;
    }

    public String getAuction_info_id() {
        return auction_info_id;
    }

    public void setAuction_info_id(String auction_info_id) {
        this.auction_info_id = auction_info_id;
    }
}
