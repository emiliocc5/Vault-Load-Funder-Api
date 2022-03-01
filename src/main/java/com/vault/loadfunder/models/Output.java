package com.vault.loadfunder.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Output implements Serializable {
    private final String id;

    @SerializedName("customer_id")
    private String customerId;

    private final boolean accepted;

    public Output(String id, String customerId, boolean accepted) {
        this.id = id;
        this.customerId = customerId;
        this.accepted = accepted;
    }

    public String getId() {
        return id;
    }

    public boolean isAccepted() {
        return accepted;
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
