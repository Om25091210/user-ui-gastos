package com.cu.gastosmerchant1.registration.new_flow.model;

import java.io.Serializable;

public class Services implements Serializable {

    private boolean Delivery;
    private boolean CashInHand;
    private boolean DineIn;
    private boolean TakeAway;
    private boolean AirConditioner;
    private boolean Wifi;

    public Services(boolean delivery, boolean cashInHand, boolean dineIn, boolean takeAway, boolean airConditioner, boolean wifi) {
        Delivery = delivery;
        CashInHand = cashInHand;
        DineIn = dineIn;
        TakeAway = takeAway;
        AirConditioner = airConditioner;
        Wifi = wifi;
    }

    public Services() {
    }

    public boolean isDelivery() {
        return Delivery;
    }

    public void setDelivery(boolean delivery) {
        Delivery = delivery;
    }

    public boolean isCashInHand() {
        return CashInHand;
    }

    public void setCashInHand(boolean cashInHand) {
        CashInHand = cashInHand;
    }

    public boolean isDineIn() {
        return DineIn;
    }

    public void setDineIn(boolean dineIn) {
        DineIn = dineIn;
    }

    public boolean isTakeAway() {
        return TakeAway;
    }

    public void setTakeAway(boolean takeAway) {
        TakeAway = takeAway;
    }

    public boolean isAirConditioner() {
        return AirConditioner;
    }

    public void setAirConditioner(boolean airConditioner) {
        AirConditioner = airConditioner;
    }

    public boolean isWifi() {
        return Wifi;
    }

    public void setWifi(boolean wifi) {
        Wifi = wifi;
    }
}
