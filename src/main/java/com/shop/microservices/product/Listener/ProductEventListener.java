package com.shop.microservices.product.Listener;

import com.shop.microservices.product.model.Product;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Listener for product-related events in the shop's system.
 * This listener is triggered before a product is converted to be saved in the MongoDB database.
 * Specifically, it ensures that a product has a valid UUID before being persisted.
 */
@Component
public class ProductEventListener {
    /**
     * Event listener that is triggered before the product is converted and persisted in the MongoDB database.
     * If the product does not have an ID, a new UUID is generated and set for the product.
     *
     * @param event The event containing the product object that is being saved.
     */
    @EventListener
    public void handleProductCreated(BeforeConvertEvent<Product> event) {
        Product product = event.getSource();  // Get the source (Product) of the event

        // If the product doesn't have an ID, generate a new UUID
        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }
    }
}
