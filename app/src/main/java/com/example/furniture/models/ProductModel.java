package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class ProductModel {
    @SerializedName(value = "productId", alternate = {"Id", "id"})
    private int Id;

    @SerializedName(value = "productName", alternate = {"Name", "name"})
    private String Name;

    @SerializedName(value = "description", alternate = {"Description"})
    private String Description;

    @SerializedName(value = "discountPrice", alternate = {"Price", "price"})
    private double Price;

    @SerializedName(value = "originalPrice", alternate = {"OldPrice", "oldPrice"})
    private double OldPrice;

    @SerializedName(value = "imageUrl", alternate = {"Image", "image"})
    private String Image;

    @SerializedName("discountPercent")
    private double discountPercent;

    @SerializedName(value = "variantId", alternate = {"VariantId"})
    private Integer variantId;

    public ProductModel() {
    }

    public ProductModel(int Id, String Name, String Description, double Price, double OldPrice, String Image) {
        this.Id = Id;
        this.Name = Name;
        this.Description = Description;
        this.Price = Price;
        this.OldPrice = OldPrice;
        this.Image = Image;
    }

    public ProductModel(int Id, String Name, String Description, double Price, double OldPrice, String Image, double discountPercent) {
        this.Id = Id;
        this.Name = Name;
        this.Description = Description;
        this.Price = Price;
        this.OldPrice = OldPrice;
        this.Image = Image;
        this.discountPercent = discountPercent;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public double getOldPrice() {
        return OldPrice;
    }

    public void setOldPrice(double OldPrice) {
        this.OldPrice = OldPrice;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public int getEffectiveVariantId() {
        return variantId != null ? variantId : Id;
    }
}
