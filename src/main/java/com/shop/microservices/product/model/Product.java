package com.shop.microservices.product.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a product in the shop's catalog.
 * This class is mapped to the "product" collection in MongoDB.
 * The product contains details such as name, description, and price.
 */
@Getter
@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    /**
     * Unique identifier for the product.
     */
    @Id
    private UUID id;

    /**
     * The name of the product.
     * The name must not be blank.
     */
    @NotBlank(message = "Product name must not be blank")
    private String name;

    /**
     * A detailed description of the product.
     * The description may be blank.
     */
    private String description;

    /**
     * The price of the product.
     * Price must be greater than or equal to 0.01 to ensure it's a valid positive value.
     */
    @NotNull(message = "Product price must not be null")
    @DecimalMin(value = "0", message = "Price must be greater than 0")
    private BigDecimal price;

    /**
     * Custom setter for price to ensure that only positive values are allowed.
     * @param price The price to set.
     * @throws IllegalArgumentException if the price is zero or negative.
     */
    public void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.price = price;
    }
}
