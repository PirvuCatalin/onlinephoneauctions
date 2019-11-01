package com.onlinephoneauctions.dto;

public class AvailablePhonesDTO {
    private String phoneId;
    private String phoneName;
    private boolean isSelected;

    public AvailablePhonesDTO() {
    }

    public AvailablePhonesDTO(String phoneId, String phoneName, boolean isSelected) {
        this.phoneId = phoneId;
        this.phoneName = phoneName;
        this.isSelected = isSelected;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
