package com.shop.microservices.product.Service.ServiceInterface;

import com.shop.microservices.product.Dto.ProductRequestDTO;
import com.shop.microservices.product.Dto.ProductResponseDTO;
import com.shop.microservices.product.Exception.EntityCreationException;
import com.shop.microservices.product.Exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

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
    ProductResponseDTO createProduct(ProductRequestDTO productRequest);

    /**
     * Retrieves a paginated list of all products.
     *
     * @param page The page number to retrieve (0-based index).
     * @param size The number of products to include per page.
     * @return A Page object containing a list of ProductResponseDTO objects for the requested page,
     *         along with pagination metadata such as total pages and total elements.
     */
    Page<ProductResponseDTO> getAllProducts(int page, int size);


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
