package com.shop.microservices.product.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * Represents a material used in the shop's product catalog.
 * This class is mapped to the "material" collection in MongoDB.
 * The material contains details such as name and description.
 */
@Getter
@Document(value = "material")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Material {

    /**
     * Unique identifier for the material.
     */
    @Id
    private UUID materialId;

    /**
     * The name of the material.
     * The name must not be blank.
     */
    @NotBlank(message = "Material name must not be blank")
    private String name;

    /**
     * A detailed description of the material.
     * The description may be blank.
     */
    private String description;
}
