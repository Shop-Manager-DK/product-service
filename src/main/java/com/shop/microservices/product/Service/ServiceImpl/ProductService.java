package com.shop.microservices.product.Service.ServiceImpl;

import com.mongodb.MongoException;
import com.shop.microservices.product.Dto.ProductRequestDTO;
import com.shop.microservices.product.Dto.ProductResponseDTO;
import com.shop.microservices.product.Exception.EntityCreationException;
import com.shop.microservices.product.Exception.UniqueConstraintViolationException;
import com.shop.microservices.product.Mapper.ProductMapper;
import com.shop.microservices.product.Service.ServiceInterface.IProductService;
import com.shop.microservices.product.Model.Product;
import com.shop.microservices.product.Repository.ProductRepository;
import com.shop.microservices.product.Utils.ProductValidationUtil;
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
    public ProductResponseDTO createProduct(@Valid ProductRequestDTO productRequest) throws EntityCreationException {
        try {

            // Validate the product request for business rules
            validateProductRequest(productRequest);

            // Map the DTO to a domain model and save it to the repository
            Product product = productMapper.productRequestDTOToProduct(productRequest);
            Product savedProduct = productRepository.save(product);

            log.info("Product created with ID: {}", savedProduct.getId());

            // Map the saved product back to a DTO and return it
            return productMapper.productToProductResponseDTO(savedProduct);

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
        // Implement deletion logic
    }

    /**
     * Validates the product request to ensure the product name is unique.
     * If the name already exists in the database, a {@link UniqueConstraintViolationException} is thrown.
     *
     * @param productRequestDTO The {@link ProductRequestDTO} containing the product details, including the name.
     * @throws UniqueConstraintViolationException If the product name is not unique, with the error code "prod.error.3102" and the field "name".
     */
    public void validateProductRequest(ProductRequestDTO productRequestDTO) throws UniqueConstraintViolationException {
        if (productValidationUtil.isProductNameUnique(productRequestDTO.getName())) {
            throw new UniqueConstraintViolationException("prod.error.3102", "name", productRequestDTO.getName());
        }
    }
}
