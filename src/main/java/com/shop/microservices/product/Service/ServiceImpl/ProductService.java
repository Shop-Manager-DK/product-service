package com.shop.microservices.product.Service.ServiceImpl;

import com.mongodb.MongoException;
import com.shop.microservices.product.Dto.ProductRequestDTO;
import com.shop.microservices.product.Dto.ProductResponseDTO;
import com.shop.microservices.product.Exception.EntityCreationException;
import com.shop.microservices.product.Mapper.ProductMapper;
import com.shop.microservices.product.Service.ServiceInterface.IProductService;
import com.shop.microservices.product.Model.Product;
import com.shop.microservices.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


/**
 * ProductService is responsible for handling business logic related to product management.
 * It interacts with the repository to perform CRUD operations and transforms domain objects
 * to/from DTOs for use in the controller layer.
 */
@Service
@Slf4j
public class ProductService implements IProductService {

    // Injected ProductRepository to interact with the database
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Constructor for ProductService class to inject dependencies via constructor injection.
     * This constructor is used to initialize the ProductRepository instance, which is responsible
     * for performing CRUD operations on the product collection in MongoDB and ProductMapper instance,
     * which is responsible for performing mapping operation on the product entity.
     *
     * @param productRepository The repository used to interact with the MongoDB database for product data.
     */
    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Creates a new product.
     * This method validates the input, persists the product, and logs the action.
     *
     * @param productRequest The DTO containing the product data.
     * @return The created Product entity, transformed to a DTO.
     */
    @Override
    @Transactional
    public ProductResponseDTO createProduct(@Valid ProductRequestDTO productRequest) throws EntityCreationException {
        try {
            log.info("Creating a new product with name: {}", productRequest.getName());

            //Map DTO to domain model using the mapper
            Product product = productMapper.productRequestDTOToProduct(productRequest);

            // Save the product to the repository
            Product savedProduct = productRepository.save(product);
            log.info("Product created with ID: {}", savedProduct.getId());

            // Mapped the saved product back to a DTO using the mapper and return it
            return productMapper.productToProductResponseDTO(savedProduct);

        } catch (MongoException ex) {
            // Log detailed error information for MongoDB exception
            log.error("MongoDB error occurred while creating product. Error Message: {}, Product Request: {}",
                    ex.getMessage(), productRequest, ex);

            // Throw a more specific exception with additional details
            throw new MongoException("Failed to save product to the database.", ex);
        } catch (Exception ex) {
            // Log unexpected errors
            log.error("Unexpected error occurred while creating product. Error Message: {}, Product Request: {}",
                    ex.getMessage(), productRequest, ex);

            // Throw a general exception for unexpected errors
            throw new EntityCreationException("prod.error.3100", ex);
        }
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return List.of();
    }

    @Override
    public ProductResponseDTO getProductById(UUID productId) {
        return null;
    }

    @Override
    public ProductResponseDTO updateProduct(UUID productId, ProductRequestDTO productRequest) {
        return null;
    }

    @Override
    public void deleteProduct(UUID productId) {

    }
}
