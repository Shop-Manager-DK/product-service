package com.shop.microservices.product.Controller;

import com.shop.microservices.product.Dto.ProductRequest;
import com.shop.microservices.product.Service.ServiceInterface.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            // Call the service layer to create a product and return a successful response
            return ResponseEntity.ok().body(productService.createProduct(productRequest));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the product: " + e.getMessage());
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllProduct(){
        try {
            return ResponseEntity.ok().body(productService.getAllProducts());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the products: " + e.getMessage());
        }
    }

}
