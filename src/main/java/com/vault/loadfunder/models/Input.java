package com.vault.loadfunder.models;

import com.google.gson.annotations.SerializedName;
import java.time.ZonedDateTime;

public class Input {
    private String id;

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("load_amount")
    private String loadAmount;

    private ZonedDateTime time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLoadAmount() {
        return loadAmount;
    }

    public void setLoadAmount(String loadAmount) {
        this.loadAmount = loadAmount;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Input{" +
                "id='" + id + '\'' +
                ", customer_id='" + customerId + '\'' +
                ", load_amount='" + loadAmount + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
