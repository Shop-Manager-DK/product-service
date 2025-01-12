package com.shop.microservices.product.Controller;

import com.shop.microservices.product.Dto.ProductRequestDTO;
import com.shop.microservices.product.Dto.ProductResponseDTO;
import com.shop.microservices.product.Exception.EntityCreationException;
import com.shop.microservices.product.Service.ServiceInterface.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller to manage product-related operations such as creating and retrieving products.
 *
 * <p>This controller exposes APIs for creating products and retrieving them with pagination.</p>
 */
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "APIs for managing products")
public class ProductController {

    private final IProductService productService;

    /**
     * Creates a new product based on the provided product data.
     *
     * <p>This endpoint accepts a {@link ProductRequestDTO} containing the product details,
     * validates and processes the request, and returns the created product in {@link ProductResponseDTO} format.</p>
     *
     * @param productRequest The product data from the client in {@link ProductRequestDTO} format.
     * @return A {@link ResponseEntity} containing the created product as {@link ProductResponseDTO}.
     * @throws EntityCreationException If there is an issue during product creation.
     */
    @Operation(summary = "Create a new product", description = "Creates a new product in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product created successfully",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = ProductResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid product data")
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Parameter(description = "Product data to be created", required = true) ProductRequestDTO productRequest) {
        // Call the service layer to create a product and return a successful response
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    /**
     * Retrieves a paginated list of products.
     *
     * <p>This endpoint fetches products with pagination support, based on the provided page number
     * and page size. Default values for pagination are page 0 and size 10 if not provided.</p>
     *
     * @param page The page number to retrieve (0-based index). Defaults to 0 if not provided.
     * @param size The number of products per page. Defaults to 10 if not provided.
     * @return A {@link ResponseEntity} containing a {@link Page} of {@link ProductResponseDTO} objects.
     */
    @Operation(summary = "Retrieve paginated products", description = "Fetches products with pagination support",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of products",
                            content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = ProductResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
            })
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number (default is 0)", required = false) int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Page size (default is 10)", required = false) int size) {

        // Fetch paginated products from the service layer
        Page<ProductResponseDTO> products = productService.getAllProducts(page, size);

        // Return the paginated result wrapped in ResponseEntity
        return ResponseEntity.ok(products);
    }
}
