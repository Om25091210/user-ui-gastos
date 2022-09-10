package com.cu.gastosmerchant1.model;

import java.io.Serializable;

public class Account_Information implements Serializable {
    private String EmailAddress;
    private String OwnerName;
    private String PhoneNumber;
    private String Rating;
    private String version="2.0";
    private String SalesCode;
    private String Shop_name;
    private String Wallet;

    public Account_Information(String emailAddress, String ownerName,
                               String phoneNumber, String rating, String salesCode, String wallet,String Shop_name) {
        EmailAddress = emailAddress;
        OwnerName = ownerName;
        PhoneNumber = phoneNumber;
        Rating = rating;
        SalesCode = salesCode;
        Wallet = wallet;
        this.Shop_name=Shop_name;
    }
    public Account_Information(String emailAddress, String ownerName,
                               String phoneNumber, String salesCode) {
        EmailAddress = emailAddress;
        OwnerName = ownerName;
        PhoneNumber = phoneNumber;
        SalesCode = salesCode;
    }

    public String getShop_name() {
        return Shop_name;
    }

    public void setShop_name(String shop_name) {
        Shop_name = shop_name;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSalesCode() {
        return SalesCode;
    }

    public void setSalesCode(String salesCode) {
        SalesCode = salesCode;
    }

    public String getWallet() {
        return Wallet;
    }

    public void setWallet(String wallet) {
        Wallet = wallet;
    }
}
