package com.shop.microservices.product.Service.ServiceImpl;

import com.mongodb.MongoException;
import com.shop.microservices.product.Dto.ProductRequestDTO;
import com.shop.microservices.product.Dto.ProductResponseDTO;
import com.shop.microservices.product.Exception.EntityCreationException;
import com.shop.microservices.product.Exception.InvalidInputException;
import com.shop.microservices.product.Exception.ResourceNotFoundException;
import com.shop.microservices.product.Exception.UniqueConstraintViolationException;
import com.shop.microservices.product.Mapper.ProductMapper;
import com.shop.microservices.product.Service.ServiceInterface.IProductService;
import com.shop.microservices.product.Model.Product;
import com.shop.microservices.product.Repository.ProductRepository;
import com.shop.microservices.product.Utils.ProductValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductValidationUtil productValidationUtil;

    /**
     * Constructs the {@link ProductService} class with the necessary dependencies.
     * The constructor initializes the {@link ProductRepository}, {@link ProductMapper},
     * and {@link ProductValidationUtil} to handle CRUD operations, entity mapping,
     * and validation tasks respectively.
     *
     * @param productRepository     The repository to interact with the MongoDB database for product data.
     * @param productMapper         The mapper to convert product entities to DTOs and vice versa.
     * @param productValidationUtil Utility class for validating product data, including name uniqueness.
     */
    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, ProductValidationUtil productValidationUtil) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productValidationUtil = productValidationUtil;
    }

    /**
     * Creates a new product by validating the product request, mapping it to a domain model,
     * and saving it to the database. If successful, returns the saved product as a DTO.
     *
     * @param productRequest The DTO containing the product data.
     * @return The created Product entity, transformed to a DTO.
     * @throws EntityCreationException If an error occurs during product creation.
     */
    @Override
    @Transactional
    public ProductResponseDTO createProduct(@Valid ProductRequestDTO productRequest){
        try {
            // Validate the product request for business rules
            validateProductRequest(productRequest);

            // Map the DTO to a domain model and save it to the repository
            Product product = productMapper.productRequestDTOToProduct(productRequest);

            Product savedProduct = productRepository.save(product);

            log.info("Product created with ID: {}", savedProduct.getId());

            // Map the saved product back to a DTO and return it
            return productMapper.productToProductResponseDTO(savedProduct);

        } catch (UniqueConstraintViolationException ex) {
            throw ex;
        } catch (MongoException ex) {
            log.error("MongoDB error occurred while creating product. Error Message: {}, Product Request: {}",
                    ex.getMessage(), productRequest, ex);
            throw new EntityCreationException("prod.error.3100", ex);
        } catch (Exception ex) {
            log.error("Unexpected error occurred while creating product. Error Message: {}, Product Request: {}",
                    ex.getMessage(), productRequest, ex);
            throw new EntityCreationException("prod.error.3101", ex);
        }
    }

    /**
     * Retrieves a paginated list of products from the database. Each product is transformed
     * into a ProductResponseDTO for the response. If no products are found, a
     * {@link ResourceNotFoundException} is thrown.
     *
     * @param page The page number to retrieve (0-based index).
     * @param size The number of products to include per page.
     * @return A Page object containing a list of {@link ProductResponseDTO} objects and pagination metadata.
     * @throws ResourceNotFoundException If no products are found in the database.
     * @throws EntityCreationException If an error occurs during product retrieval, such as a database issue.
     */
    public Page<ProductResponseDTO> getAllProducts(int page, int size) {
        try {
            // Fetch paginated products
            Page<Product> productPage = productRepository.findAll(PageRequest.of(page, size));

            if (productPage.isEmpty()) {
                throw new ResourceNotFoundException("prod.error.3104");
            }

            // Convert each Product to a ProductResponseDTO and return the page
            return productPage.map(productMapper::productToProductResponseDTO);

        } catch (MongoException ex) {
            log.error("MongoDB error occurred while retrieving products. Error Message: {}", ex.getMessage(), ex);
            throw new EntityCreationException("prod.error.3106", ex);
        } catch (Exception ex) {
            log.error("Unexpected error occurred while retrieving products. Error Message: {}", ex.getMessage(), ex);
            throw new EntityCreationException("prod.error.3107", ex);
        }
    }

    /**
     * Retrieves a product by its ID.
     * <p>
     * Validates the input product ID, parses it from a string, and fetches the corresponding product.
     * Throws an exception if the input is invalid or the product is not found.
     * </p>
     *
     * @param productIdStr The product ID as a string (UUID format) received from the client.
     * @return A {@link ProductResponseDTO} containing the product details.
     * @throws InvalidInputException      If the input string is null, empty, or not a valid UUID.
     * @throws ResourceNotFoundException If no product is found for the given UUID.
     */
    @Override
    public ProductResponseDTO getProductById(String productIdStr) {
        // Validate string input
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            throw new InvalidInputException("prod.error.3108");
        }

        UUID productId;
        try {
            // Attempt to parse the input string into a UUID
            productId = UUID.fromString(productIdStr);
        } catch (IllegalArgumentException ex) {
            throw new InvalidInputException("prod.error.3109"); // Add a new error code for invalid UUID format
        }

        // Fetch product by ID or throw a custom exception
        Product retrievedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("prod.error.3105", productId));

        // Map the product entity to DTO
        return productMapper.productToProductResponseDTO(retrievedProduct);
    }



    @Override
    public ProductResponseDTO updateProduct(UUID productId, ProductRequestDTO productRequest) {
        return null;
    }

    @Override
    public void deleteProduct(UUID productId) {
        // Implement deletion logic
    }

    /**
     * Validates the product request to ensure the product name is unique.
     * If the name already exists in the database, a {@link UniqueConstraintViolationException} is thrown.
     *
     * @param productRequestDTO The {@link ProductRequestDTO} containing the product details, including the name.
     * @throws UniqueConstraintViolationException If the product name is not unique, with the error code "prod.error.3102" and the field "name".
     */
    public void validateProductRequest(ProductRequestDTO productRequestDTO){
        if (!productValidationUtil.isProductNameUnique(productRequestDTO.getName())) {
            throw new UniqueConstraintViolationException("prod.error.3102", "name", productRequestDTO.getName());
        }
    }
}
