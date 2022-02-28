package com.vault.loadfunder.models;

public class ClientControl2 {

    private Double dayAmount;
    private int dayLoads;
    private Double weekAmount;
    private int lastOperationDay;
    private int lastOperationWeek;

    public ClientControl2(Double dayAmount, int dayLoads, Double weekAmount, int lastOperationDay, int lastOperationWeek) {
        this.dayAmount = dayAmount;
        this.dayLoads = dayLoads;
        this.weekAmount = weekAmount;
        this.lastOperationDay = lastOperationDay;
        this.lastOperationWeek = lastOperationWeek;
    }

    public Double getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(Double dayAmount) {
        this.dayAmount = dayAmount;
    }

    public int getDayLoads() {
        return dayLoads;
    }

    public void setDayLoads(int dayLoads) {
        this.dayLoads = dayLoads;
    }

    public Double getWeekAmount() {
        return weekAmount;
    }

    public void setWeekAmount(Double weekAmount) {
        this.weekAmount = weekAmount;
    }

    public int getLastOperationDay() {
        return lastOperationDay;
    }

    public void setLastOperationDay(int lastOperationDay) {
        this.lastOperationDay = lastOperationDay;
    }

    public int getLastOperationWeek() {
        return lastOperationWeek;
    }

    public void setLastOperationWeek(int lastOperationWeek) {
        this.lastOperationWeek = lastOperationWeek;
    }
}
