package com.vault.loadfunder.models;

public class ClientControl {
    private Double dayAmount;
    private int dayLoads;
    private Double weekAmount;

    public ClientControl(Double dayAmount, int dayLoads, Double weekAmount) {
        this.dayAmount = dayAmount;
        this.dayLoads = dayLoads;
        this.weekAmount = weekAmount;
    }

    public ClientControl(Double weekAmount) {
        this.weekAmount = weekAmount;
    }

    public Double getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(Double dayAmount) {
        this.dayAmount = dayAmount;
    }

    public Double getWeekAmount() {
        return weekAmount;
    }

    public void setWeekAmount(Double weekAmount) {
        this.weekAmount = weekAmount;
    }

    public int getDayLoads() {
        return dayLoads;
    }

    public void setDayLoads(int dayLoads) {
        this.dayLoads = dayLoads;
    }
}
