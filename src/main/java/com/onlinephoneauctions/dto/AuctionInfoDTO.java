package com.onlinephoneauctions.dto;

public class AuctionInfoDTO {

    private String id;
    private String title;
    private double starting_price;
    private double target_price;
    private double current_price_bidded;
    private String datetime_start;
    private String datetime_end;
    private String seller_id;
    private String seller_name;
    private String additional_info;
    private boolean is_successfully_done;

    public AuctionInfoDTO() {
    }

    public AuctionInfoDTO(String id, String title, double starting_price, double target_price, double current_price_bidded, String datetime_start, String datetime_end, String seller_id, String seller_name, String additional_info) {
        this.id = id;
        this.title = title;
        this.starting_price = starting_price;
        this.target_price = target_price;
        this.current_price_bidded = current_price_bidded;
        this.datetime_start = datetime_start;
        this.datetime_end = datetime_end;
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.additional_info = additional_info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getStarting_price() {
        return starting_price;
    }

    public void setStarting_price(double starting_price) {
        this.starting_price = starting_price;
    }

    public double getTarget_price() {
        return target_price;
    }

    public void setTarget_price(double target_price) {
        this.target_price = target_price;
    }

    public double getCurrent_price_bidded() {
        return current_price_bidded;
    }

    public void setCurrent_price_bidded(double current_price_bidded) {
        this.current_price_bidded = current_price_bidded;
    }

    public String getDatetime_start() {
        return datetime_start;
    }

    public void setDatetime_start(String datetime_start) {
        this.datetime_start = datetime_start;
    }

    public String getDatetime_end() {
        return datetime_end;
    }

    public void setDatetime_end(String datetime_end) {
        this.datetime_end = datetime_end;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    @Override
    public String toString() {
        return "AuctionInfoDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", starting_price=" + starting_price +
                ", target_price=" + target_price +
                ", current_price_bidded=" + current_price_bidded +
                ", datetime_start=" + datetime_start +
                ", datetime_end=" + datetime_end +
                ", seller_id='" + seller_id + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", additional_info='" + additional_info + '\'' +
                '}';
    }

    public boolean isIs_successfully_done() {
        return is_successfully_done;
    }

    public void setIs_successfully_done(boolean is_successfully_done) {
        this.is_successfully_done = is_successfully_done;
    }
}