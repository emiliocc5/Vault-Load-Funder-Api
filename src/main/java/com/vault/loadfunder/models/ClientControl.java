package com.vault.loadfunder.models;

import java.math.BigDecimal;

public class ClientControl {

    private BigDecimal dayAmount;
    private int dayLoads;
    private BigDecimal weekAmount;
    private int lastOperationDay;
    private int lastOperationWeek;

    public ClientControl(BigDecimal dayAmount, int dayLoads, BigDecimal weekAmount, int lastOperationDay, int lastOperationWeek) {
        this.dayAmount = dayAmount;
        this.dayLoads = dayLoads;
        this.weekAmount = weekAmount;
        this.lastOperationDay = lastOperationDay;
        this.lastOperationWeek = lastOperationWeek;
    }

    public BigDecimal getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(BigDecimal dayAmount) {
        this.dayAmount = dayAmount;
    }

    public int getDayLoads() {
        return dayLoads;
    }

    public void setDayLoads(int dayLoads) {
        this.dayLoads = dayLoads;
    }

    public BigDecimal getWeekAmount() {
        return weekAmount;
    }

    public void setWeekAmount(BigDecimal weekAmount) {
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
