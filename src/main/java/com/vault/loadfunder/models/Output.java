package com.vault.loadfunder.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Output implements Serializable {
    private String id;

    @SerializedName("customer_id")
    private String customerId;

    private boolean accepted;

    public Output() {
    }

    public Output(String id, String customerId, boolean accepted) {
        this.id = id;
        this.customerId = customerId;
        this.accepted = accepted;
    }

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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"" +
                ",\"customer_id\":\"" + customerId + "\"" +
                ",\"accepted\":" + accepted +
                '}';
    }
}
