package com.shop.microservices.product.Listener;

import com.shop.microservices.product.Model.Category;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Listener for category-related events in the shop's system.
 * This listener is triggered before a category is converted to be saved in the MongoDB database.
 * Specifically, it ensures that a category has a valid UUID before being persisted.
 */
@Component
public class CategoryEventListener {

    /**
     * Event listener that is triggered before the category is converted and persisted in the MongoDB database.
     * If the category does not have an ID, a new UUID is generated and set for the category.
     *
     * @param event The event containing the category object that is being saved.
     */
    @EventListener
    public void handleCategoryCreated(BeforeConvertEvent<Category> event) {
        Category category = event.getSource();  // Get the source (Category) of the event

        // If the category doesn't have an ID, generate a new UUID
        if (category.getCategoryId() == null) {
            category.setCategoryId(UUID.randomUUID());
        }
    }
}
