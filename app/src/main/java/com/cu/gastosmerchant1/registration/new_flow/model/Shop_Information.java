package com.cu.gastosmerchant1.registration.new_flow.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Shop_Information implements Serializable {

    private String CreationTimeStamp;
    private String ShopAddress;
    private String ShopState;
    private String ShopDistrict;
    private String ShopArea;
    private String ShopName;
    private String Category;
    private String SubCategory;
    private String ShopAddressLongitude;
    private String ShopAddressLatitude;
    private String CoverPhotoUri;
    private String ProfilePhotoUri;
    private String ShopPhotoUri;
    private String ShopPhotoUri1;
    private String ShopPhotoUri2;
    private String CatalogueUri;
    private String CatalogueUri1;
    private String CatalogueUri2;
    private Services services;
    private ArrayList<Discount> discounts;

    public Shop_Information(String creationTimeStamp, String shopAddress, String shopState, String shopDistrict, String shopArea, String shopName, String category, String subCategory, String shopAddressLongitude, String shopAddressLatitude, String coverPhotoUri, String profilePhotoUri, String shopPhotoUri, String shopPhotoUri1, String shopPhotoUri2, String catalogueUri, String catalogueUri1, String catalogueUri2, Services services, ArrayList<Discount> discounts) {
        CreationTimeStamp = creationTimeStamp;
        ShopAddress = shopAddress;
        ShopState = shopState;
        ShopDistrict = shopDistrict;
        ShopArea = shopArea;
        ShopName = shopName;
        Category = category;
        SubCategory = subCategory;
        ShopAddressLongitude = shopAddressLongitude;
        ShopAddressLatitude = shopAddressLatitude;
        CoverPhotoUri = coverPhotoUri;
        ProfilePhotoUri = profilePhotoUri;
        ShopPhotoUri = shopPhotoUri;
        ShopPhotoUri1 = shopPhotoUri1;
        ShopPhotoUri2 = shopPhotoUri2;
        CatalogueUri = catalogueUri;
        CatalogueUri1 = catalogueUri1;
        CatalogueUri2 = catalogueUri2;
        this.services = services;
        this.discounts = discounts;
    }

    public Shop_Information() {
    }

    public String getCreationTimeStamp() {
        return CreationTimeStamp;
    }

    public void setCreationTimeStamp(String creationTimeStamp) {
        CreationTimeStamp = creationTimeStamp;
    }

    public String getShopAddress() {
        return ShopAddress;
    }

    public void setShopAddress(String shopAddress) {
        ShopAddress = shopAddress;
    }

    public String getShopState() {
        return ShopState;
    }

    public void setShopState(String shopState) {
        ShopState = shopState;
    }

    public String getShopDistrict() {
        return ShopDistrict;
    }

    public void setShopDistrict(String shopDistrict) {
        ShopDistrict = shopDistrict;
    }

    public String getShopArea() {
        return ShopArea;
    }

    public void setShopArea(String shopArea) {
        ShopArea = shopArea;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public String getShopAddressLongitude() {
        return ShopAddressLongitude;
    }

    public void setShopAddressLongitude(String shopAddressLongitude) {
        ShopAddressLongitude = shopAddressLongitude;
    }

    public String getShopAddressLatitude() {
        return ShopAddressLatitude;
    }

    public void setShopAddressLatitude(String shopAddressLatitude) {
        ShopAddressLatitude = shopAddressLatitude;
    }

    public String getCoverPhotoUri() {
        return CoverPhotoUri;
    }

    public void setCoverPhotoUri(String coverPhotoUri) {
        CoverPhotoUri = coverPhotoUri;
    }

    public String getProfilePhotoUri() {
        return ProfilePhotoUri;
    }

    public void setProfilePhotoUri(String profilePhotoUri) {
        ProfilePhotoUri = profilePhotoUri;
    }

    public String getShopPhotoUri() {
        return ShopPhotoUri;
    }

    public void setShopPhotoUri(String shopPhotoUri) {
        ShopPhotoUri = shopPhotoUri;
    }

    public String getShopPhotoUri1() {
        return ShopPhotoUri1;
    }

    public void setShopPhotoUri1(String shopPhotoUri1) {
        ShopPhotoUri1 = shopPhotoUri1;
    }

    public String getShopPhotoUri2() {
        return ShopPhotoUri2;
    }

    public void setShopPhotoUri2(String shopPhotoUri2) {
        ShopPhotoUri2 = shopPhotoUri2;
    }

    public String getCatalogueUri() {
        return CatalogueUri;
    }

    public void setCatalogueUri(String catalogueUri) {
        CatalogueUri = catalogueUri;
    }

    public String getCatalogueUri1() {
        return CatalogueUri1;
    }

    public void setCatalogueUri1(String catalogueUri1) {
        CatalogueUri1 = catalogueUri1;
    }

    public String getCatalogueUri2() {
        return CatalogueUri2;
    }

    public void setCatalogueUri2(String catalogueUri2) {
        CatalogueUri2 = catalogueUri2;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(ArrayList<Discount> discounts) {
        this.discounts = discounts;
    }
}
