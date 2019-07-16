package com.example.aroundapplcation.model;

import java.util.Objects;

public class AdvertiserBusinessCard {
    private BusinessCard businessCard;
    private String endpointId;

    public BusinessCard getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(BusinessCard businessCard) {
        this.businessCard = businessCard;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdvertiserBusinessCard that = (AdvertiserBusinessCard) o;
        return Objects.equals(businessCard, that.businessCard) &&
                Objects.equals(endpointId, that.endpointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessCard, endpointId);
    }
}
