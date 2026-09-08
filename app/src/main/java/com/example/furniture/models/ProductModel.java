package com.example.furniture.models;
public class ProductModel {
    private int Id;
    private String Name;
    private String Description;
    private double Price;
    private double OldPrice;
    private String Image;

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
}