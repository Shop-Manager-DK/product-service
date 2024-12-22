package com.shop.microservices.product.model;

import io.swagger.annotations.ApiModel;
import lombok.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * Represents a product category in the shop's catalog.
 * This class is mapped to the "category" collection in MongoDB.
 * The category contains details such as its name and description.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "Represents a category of products in the shop's catalog.")
public class Category {

    /**
     * Unique identifier for the category.
     */
    private UUID categoryId;

    /**
     * The name of the category.
     * The name must not be blank.
     */
    @NotBlank(message = "Category name must not be blank")
    private String name;

    /**
     * A detailed description of the category.
     * The description may be blank.
     */
    private String description;
}
