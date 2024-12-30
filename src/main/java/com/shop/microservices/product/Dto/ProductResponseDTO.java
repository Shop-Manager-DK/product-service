package com.shop.microservices.product.Dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents the response object for a product in the shop's catalog.
 * This DTO is used to transfer product data from the backend to the client.
 */
@ApiModel(description = "Represents the response object for a product in the shop's catalog.")
public record ProductResponseDTO(

        /**
         * Unique identifier for the product.
         */
        @ApiModelProperty(notes = "Unique identifier for the product", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        /**
         * The name of the product.
         */
        @ApiModelProperty(notes = "The name of the product", required = true, example = "Wireless Mouse")
        String name,

        /**
         * A detailed description of the product.
         */
        @ApiModelProperty(notes = "A detailed description of the product", required = false, example = "A high-precision wireless mouse with ergonomic design.")
        String description,

        /**
         * The price of the product.
         */
        @ApiModelProperty(notes = "The price of the product", required = true, example = "29.99")
        BigDecimal price

) {}
