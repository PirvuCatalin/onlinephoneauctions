package com.onlinephoneauctions.dto;

public class ReviewsDTO {
    private String id;
    private String seller;
    private String auction;
    private String buyer;
    private String auctionEndDatetime;
    private double buyPrice;
    private int stars;
    private String review;

    public ReviewsDTO() {
    }

    public ReviewsDTO(String id, String seller, String auction, String buyer, String auctionEndDatetime, double buyPrice, int stars, String review) {
        this.id = id;
        this.seller = seller;
        this.auction = auction;
        this.buyer = buyer;
        this.auctionEndDatetime = auctionEndDatetime;
        this.buyPrice = buyPrice;
        this.stars = stars;
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getAuction() {
        return auction;
    }

    public void setAuction(String auction) {
        this.auction = auction;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getAuctionEndDatetime() {
        return auctionEndDatetime;
    }

    public void setAuctionEndDatetime(String auctionEndDatetime) {
        this.auctionEndDatetime = auctionEndDatetime;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
