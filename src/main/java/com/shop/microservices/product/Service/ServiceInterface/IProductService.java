package com.shop.microservices.product.Service.ServiceInterface;

import com.shop.microservices.product.Dto.ProductRequestDTO;
import com.shop.microservices.product.Dto.ProductResponseDTO;
import com.shop.microservices.product.Exception.EntityCreationException;

import java.util.List;
import java.util.UUID;

/**
 * IProductService interface defines the contract for the ProductService class.
 * It declares the essential CRUD operations for managing products.
 */
public interface IProductService {

    /**
     * Creates a new product.
     *
     * @param productRequest The DTO containing the product data.
     * @return A ProductResponseDTO representing the created product.
     */
    ProductResponseDTO createProduct(ProductRequestDTO productRequest) throws EntityCreationException;

    /**
     * Retrieves all products.
     *
     * @return A list of ProductResponseDTO objects representing all products.
     */
    List<ProductResponseDTO> getAllProducts();

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The ID of the product to fetch.
     * @return A ProductResponseDTO representing the requested product.
     */
    ProductResponseDTO getProductById(UUID productId);

    /**
     * Updates an existing product.
     *
     * @param productId      The ID of the product to update.
     * @param productRequest The DTO containing the updated product data.
     * @return A ProductResponseDTO representing the updated product.
     */
    ProductResponseDTO updateProduct(UUID productId, ProductRequestDTO productRequest);

    /**
     * Deletes a product by its ID.
     *
     * @param productId The ID of the product to delete.
     */
    void deleteProduct(UUID productId);
}
