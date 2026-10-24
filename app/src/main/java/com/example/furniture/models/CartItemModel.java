package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class CartItemModel {
    @SerializedName(value = "cartItemId", alternate = {"id", "Id", "CartItemId"})
    private int cartItemId;

    @SerializedName(value = "cartId", alternate = {"CartId"})
    private int cartId;

    @SerializedName(value = "variantId", alternate = {"VariantId"})
    private int variantId;

    @SerializedName(value = "quantity", alternate = {"Quantity"})
    private int quantity = 1;

    @SerializedName(value = "unitPrice", alternate = {"price", "Price", "UnitPrice"})
    private double unitPrice;

    @SerializedName(value = "subTotal", alternate = {"totalPrice", "SubTotal", "TotalPrice"})
    private double subTotal;

    @SerializedName(value = "product", alternate = {"Product"})
    private ProductDetails product;

    @SerializedName(value = "variant", alternate = {"Variant"})
    private VariantDetails variant;

    public CartItemModel() {
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getVariantId() {
        if (variantId > 0) {
            return variantId;
        }
        if (variant != null && variant.getVariantId() > 0) {
            return variant.getVariantId();
        }
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        if (unitPrice > 0) {
            return unitPrice;
        }
        if (variant != null && variant.getDiscountPrice() > 0) {
            return variant.getDiscountPrice();
        }
        if (variant != null && variant.getPrice() > 0) {
            return variant.getPrice();
        }
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getPrice() {
        return getUnitPrice();
    }

    public double getSubTotal() {
        return subTotal > 0 ? subTotal : getUnitPrice() * quantity;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public String getProductName() {
        StringBuilder name = new StringBuilder();
        if (product != null && product.getProductName() != null && !product.getProductName().trim().isEmpty()) {
            name.append(product.getProductName());
        }
        if (variant != null && variant.getColor() != null && !variant.getColor().trim().isEmpty()) {
            if (name.length() > 0) {
                name.append(" (").append(variant.getColor()).append(")");
            } else {
                name.append(variant.getColor());
            }
        }
        return name.length() > 0 ? name.toString() : "Sản phẩm";
    }

    public String getImageUrl() {
        if (product != null && product.getImageUrl() != null && !product.getImageUrl().trim().isEmpty()) {
            return product.getImageUrl();
        }
        return null;
    }

    public ProductDetails getProduct() {
        return product;
    }

    public void setProduct(ProductDetails product) {
        this.product = product;
    }

    public VariantDetails getVariant() {
        return variant;
    }

    public void setVariant(VariantDetails variant) {
        this.variant = variant;
    }

    public static class ProductDetails {
        @SerializedName(value = "productId", alternate = {"id", "Id", "ProductId"})
        private int productId;

        @SerializedName(value = "productName", alternate = {"name", "Name", "title", "ProductName"})
        private String productName;

        @SerializedName(value = "description", alternate = {"Description"})
        private String description;

        @SerializedName(value = "imageUrl", alternate = {"image", "Image", "ImageUrl"})
        private String imageUrl;

        public int getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getDescription() {
            return description;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public static class VariantDetails {
        @SerializedName(value = "variantId", alternate = {"id", "Id", "VariantId"})
        private int variantId;

        @SerializedName(value = "color", alternate = {"Color"})
        private String color;

        @SerializedName(value = "upholsteryMaterial", alternate = {"UpholsteryMaterial"})
        private String upholsteryMaterial;

        @SerializedName(value = "price", alternate = {"Price"})
        private double price;

        @SerializedName(value = "discountPrice", alternate = {"DiscountPrice"})
        private double discountPrice;

        @SerializedName(value = "discountPercent", alternate = {"DiscountPercent"})
        private double discountPercent;

        @SerializedName(value = "isOnSale", alternate = {"IsOnSale"})
        private boolean isOnSale;

        @SerializedName(value = "stockQuantity", alternate = {"StockQuantity"})
        private int stockQuantity;

        public int getVariantId() {
            return variantId;
        }

        public String getColor() {
            return color;
        }

        public String getUpholsteryMaterial() {
            return upholsteryMaterial;
        }

        public double getPrice() {
            return price;
        }

        public double getDiscountPrice() {
            return discountPrice;
        }

        public double getDiscountPercent() {
            return discountPercent;
        }

        public boolean isOnSale() {
            return isOnSale;
        }

        public int getStockQuantity() {
            return stockQuantity;
        }
    }
}
