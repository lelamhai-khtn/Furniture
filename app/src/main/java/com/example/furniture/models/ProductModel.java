package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class ProductModel {
    @SerializedName(value = "productId", alternate = {"Id", "id", "ProductId"})
    private int productId;

    @SerializedName(value = "productName", alternate = {"Name", "name", "ProductName"})
    private String productName;

    @SerializedName(value = "description", alternate = {"Description"})
    private String description;

    @SerializedName(value = "categoryId", alternate = {"CategoryId"})
    private Integer categoryId;

    @SerializedName(value = "categoryName", alternate = {"CategoryName"})
    private String categoryName;

    @SerializedName(value = "imageUrl", alternate = {"Image", "image", "ImageUrl"})
    private String imageUrl;

    @SerializedName(value = "discountPercent", alternate = {"DiscountPercent"})
    private double discountPercent;

    @SerializedName(value = "originalPrice", alternate = {"OldPrice", "oldPrice", "OriginalPrice"})
    private double originalPrice;

    @SerializedName(value = "discountPrice", alternate = {"Price", "price", "DiscountPrice"})
    private double discountPrice;

    @SerializedName(value = "color", alternate = {"Color"})
    private String color;

    @SerializedName(value = "upholsteryMaterial", alternate = {"UpholsteryMaterial"})
    private String upholsteryMaterial;

    @SerializedName(value = "stockQuantity", alternate = {"StockQuantity"})
    private int stockQuantity;

    @SerializedName(value = "baseMaterial", alternate = {"BaseMaterial"})
    private String baseMaterial;

    @SerializedName(value = "createdAt", alternate = {"CreatedAt"})
    private String createdAt;

    @SerializedName(value = "isWishlisted", alternate = {"IsWishlisted", "isFavorite", "IsFavorite", "favorite", "Favorite"})
    private boolean isWishlisted;

    @SerializedName(value = "dimensions", alternate = {"Dimensions"})
    private ProductDimensions dimensions;

    public ProductModel() {
    }

    public ProductModel(int productId, String productName, String description, double discountPrice, double originalPrice, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.discountPrice = discountPrice;
        this.originalPrice = originalPrice;
        this.imageUrl = imageUrl;
    }

    public ProductModel(int productId, String productName, String description, double discountPrice, double originalPrice, String imageUrl, double discountPercent) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.discountPrice = discountPrice;
        this.originalPrice = originalPrice;
        this.imageUrl = imageUrl;
        this.discountPercent = discountPercent;
    }

    public int getId() {
        return productId;
    }

    public void setId(int id) {
        this.productId = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return productName;
    }

    public void setName(String name) {
        this.productName = name;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getOldPrice() {
        return originalPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.originalPrice = oldPrice;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getPrice() {
        return discountPrice;
    }

    public void setPrice(double price) {
        this.discountPrice = price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUpholsteryMaterial() {
        return upholsteryMaterial;
    }

    public void setUpholsteryMaterial(String upholsteryMaterial) {
        this.upholsteryMaterial = upholsteryMaterial;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getBaseMaterial() {
        return baseMaterial;
    }

    public void setBaseMaterial(String baseMaterial) {
        this.baseMaterial = baseMaterial;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        isWishlisted = wishlisted;
    }

    public ProductDimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(ProductDimensions dimensions) {
        this.dimensions = dimensions;
    }

    public int getEffectiveVariantId() {
        return productId;
    }

    public static class ProductDimensions {
        @SerializedName(value = "lengthCm", alternate = {"LengthCm"})
        private double lengthCm;

        @SerializedName(value = "widthCm", alternate = {"WidthCm"})
        private double widthCm;

        @SerializedName(value = "heightCm", alternate = {"HeightCm"})
        private double heightCm;

        @SerializedName(value = "weightKg", alternate = {"WeightKg"})
        private double weightKg;

        public ProductDimensions() {
        }

        public ProductDimensions(double lengthCm, double widthCm, double heightCm, double weightKg) {
            this.lengthCm = lengthCm;
            this.widthCm = widthCm;
            this.heightCm = heightCm;
            this.weightKg = weightKg;
        }

        public double getLengthCm() {
            return lengthCm;
        }

        public void setLengthCm(double lengthCm) {
            this.lengthCm = lengthCm;
        }

        public double getWidthCm() {
            return widthCm;
        }

        public void setWidthCm(double widthCm) {
            this.widthCm = widthCm;
        }

        public double getHeightCm() {
            return heightCm;
        }

        public void setHeightCm(double heightCm) {
            this.heightCm = heightCm;
        }

        public double getWeightKg() {
            return weightKg;
        }

        public void setWeightKg(double weightKg) {
            this.weightKg = weightKg;
        }
    }
}
