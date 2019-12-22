package com.onlinephoneauctions.dto;

public class BidDTO {
    private String id;
    private String auction_info_id;
    private String buyer_id;
    private String buyer_name;
    private String datetime_bidded;
    private double price_bidded;
    private boolean isHighest;
    private boolean isWinner;

    public BidDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuction_info_id() {
        return auction_info_id;
    }

    public void setAuction_info_id(String auction_info_id) {
        this.auction_info_id = auction_info_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getDatetime_bidded() {
        return datetime_bidded;
    }

    public void setDatetime_bidded(String datetime_bidded) {
        this.datetime_bidded = datetime_bidded;
    }

    public double getPrice_bidded() {
        return price_bidded;
    }

    public void setPrice_bidded(double price_bidded) {
        this.price_bidded = price_bidded;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public boolean isHighest() {
        return isHighest;
    }

    public void setHighest(boolean highest) {
        isHighest = highest;
    }
}
