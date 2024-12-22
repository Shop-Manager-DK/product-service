package com.shop.microservices.product.Listener;

import com.shop.microservices.product.model.Material;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Listener for material-related events in the shop's system.
 * This listener is triggered before a material is converted to be saved in the MongoDB database.
 * Specifically, it ensures that a material has a valid UUID before being persisted.
 */
@Component
public class MaterialEventListener {

    /**
     * Event listener that is triggered before the material is converted and persisted in the MongoDB database.
     * If the material does not have an ID, a new UUID is generated and set for the material.
     *
     * @param event The event containing the material object that is being saved.
     */
    @EventListener
    public void handleMaterialCreated(BeforeConvertEvent<Material> event) {
        Material material = event.getSource();  // Get the source (Material) of the event

        // If the material doesn't have an ID, generate a new UUID
        if (material.getMaterialId() == null) {
            material.setMaterialId(UUID.randomUUID());
        }
    }
}
