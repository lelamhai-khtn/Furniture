package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModel {
    @SerializedName(value = "productId", alternate = {"Id", "id", "ProductId"})
    private int Id;

    @SerializedName(value = "variantId", alternate = {"VariantId"})
    private Integer variantId;

    @SerializedName(value = "productName", alternate = {"Name", "name", "ProductName"})
    private String Name;

    @SerializedName(value = "description", alternate = {"Description"})
    private String Description;

    @SerializedName(value = "categoryId", alternate = {"CategoryId"})
    private Integer categoryId;

    @SerializedName(value = "categoryName", alternate = {"CategoryName"})
    private String categoryName;

    @SerializedName(value = "imageUrl", alternate = {"Image", "image", "ImageUrl"})
    private String Image;

    @SerializedName(value = "discountPercent", alternate = {"DiscountPercent"})
    private double discountPercent;

    @SerializedName(value = "originalPrice", alternate = {"OldPrice", "oldPrice", "OriginalPrice"})
    private double OldPrice;

    @SerializedName(value = "discountPrice", alternate = {"Price", "price", "DiscountPrice"})
    private double Price;

    @SerializedName(value = "baseMaterial", alternate = {"BaseMaterial"})
    private String baseMaterial;

    @SerializedName(value = "variantsCount", alternate = {"VariantsCount"})
    private Integer variantsCount;

    @SerializedName(value = "variants", alternate = {"Variants"})
    private List<ProductVariant> variants;

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

    public int getProductId() {
        return Id;
    }

    public void setProductId(int productId) {
        this.Id = productId;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getProductName() {
        return Name;
    }

    public void setProductName(String productName) {
        this.Name = productName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
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

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public double getDiscountPrice() {
        return Price;
    }

    public void setDiscountPrice(double discountPrice) {
        this.Price = discountPrice;
    }

    public double getOldPrice() {
        return OldPrice;
    }

    public void setOldPrice(double OldPrice) {
        this.OldPrice = OldPrice;
    }

    public double getOriginalPrice() {
        return OldPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.OldPrice = originalPrice;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getImageUrl() {
        return Image;
    }

    public void setImageUrl(String imageUrl) {
        this.Image = imageUrl;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getBaseMaterial() {
        return baseMaterial;
    }

    public void setBaseMaterial(String baseMaterial) {
        this.baseMaterial = baseMaterial;
    }

    public Integer getVariantsCount() {
        return variantsCount;
    }

    public void setVariantsCount(Integer variantsCount) {
        this.variantsCount = variantsCount;
    }

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    public int getEffectiveVariantId() {
        if (variantId != null && variantId > 0) {
            return variantId;
        }
        if (variants != null && !variants.isEmpty()) {
            for (ProductVariant v : variants) {
                if (v != null && v.getVariantId() > 0) {
                    return v.getVariantId();
                }
            }
        }
        return Id;
    }

    public static class ProductVariant {
        @SerializedName(value = "variantId", alternate = {"VariantId", "id", "Id"})
        private int variantId;

        @SerializedName(value = "color", alternate = {"Color"})
        private String color;

        @SerializedName(value = "upholsteryMaterial", alternate = {"UpholsteryMaterial"})
        private String upholsteryMaterial;

        @SerializedName(value = "price", alternate = {"Price", "originalPrice", "OriginalPrice"})
        private double price;

        @SerializedName(value = "discountPrice", alternate = {"DiscountPrice"})
        private double discountPrice;

        @SerializedName(value = "discountPercent", alternate = {"DiscountPercent"})
        private double discountPercent;

        @SerializedName(value = "isOnSale", alternate = {"IsOnSale"})
        private boolean isOnSale;

        @SerializedName(value = "stockQuantity", alternate = {"StockQuantity"})
        private int stockQuantity;

        @SerializedName(value = "imageUrl", alternate = {"Image", "image", "ImageUrl"})
        private String imageUrl;

        public ProductVariant() {
        }

        public int getVariantId() {
            return variantId;
        }

        public void setVariantId(int variantId) {
            this.variantId = variantId;
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(double discountPrice) {
            this.discountPrice = discountPrice;
        }

        public double getDiscountPercent() {
            return discountPercent;
        }

        public void setDiscountPercent(double discountPercent) {
            this.discountPercent = discountPercent;
        }

        public boolean isOnSale() {
            return isOnSale;
        }

        public void setOnSale(boolean onSale) {
            isOnSale = onSale;
        }

        public int getStockQuantity() {
            return stockQuantity;
        }

        public void setStockQuantity(int stockQuantity) {
            this.stockQuantity = stockQuantity;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
