package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class CartItemModel {
    @SerializedName(value = "cartId", alternate = {"cartItemId", "id", "Id", "CartId", "CartItemId"})
    private int cartId;

    @SerializedName(value = "productId", alternate = {"ProductId"})
    private int productId;

    @SerializedName(value = "quantity", alternate = {"Quantity"})
    private int quantity = 1;

    @SerializedName(value = "unitPrice", alternate = {"price", "Price", "UnitPrice"})
    private double unitPrice;

    @SerializedName(value = "subTotal", alternate = {"totalPrice", "SubTotal", "TotalPrice"})
    private double subTotal;

    @SerializedName(value = "product", alternate = {"Product"})
    private ProductDetails product;

    public CartItemModel() {
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        if (productId > 0) {
            return productId;
        }
        if (product != null && product.getProductId() > 0) {
            return product.getProductId();
        }
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
        if (product != null && product.getDiscountPrice() != null && product.getDiscountPrice() > 0) {
            return product.getDiscountPrice();
        }
        if (product != null && product.getPrice() > 0) {
            return product.getPrice();
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
        if (product == null) {
            return "Sản phẩm";
        }
        StringBuilder name = new StringBuilder();
        if (product.getProductName() != null && !product.getProductName().trim().isEmpty()) {
            name.append(product.getProductName());
        }
        if (product.getColor() != null && !product.getColor().trim().isEmpty()) {
            if (name.length() > 0) {
                name.append(" (").append(product.getColor()).append(")");
            } else {
                name.append(product.getColor());
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

    public static class ProductDetails {
        @SerializedName(value = "productId", alternate = {"id", "Id", "ProductId"})
        private int productId;

        @SerializedName(value = "productName", alternate = {"name", "Name", "title", "ProductName"})
        private String productName;

        @SerializedName(value = "description", alternate = {"Description"})
        private String description;

        @SerializedName(value = "price", alternate = {"Price", "originalPrice", "OriginalPrice"})
        private double price;

        @SerializedName(value = "discountPrice", alternate = {"DiscountPrice"})
        private Double discountPrice;

        @SerializedName(value = "color", alternate = {"Color"})
        private String color;

        @SerializedName(value = "upholsteryMaterial", alternate = {"UpholsteryMaterial"})
        private String upholsteryMaterial;

        @SerializedName(value = "stockQuantity", alternate = {"StockQuantity"})
        private int stockQuantity;

        @SerializedName(value = "imageUrl", alternate = {"image", "Image", "ImageUrl"})
        private String imageUrl;

        public ProductDetails() {
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public Double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(Double discountPrice) {
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

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
