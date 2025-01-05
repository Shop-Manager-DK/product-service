package com.shop.microservices.product.Controller;

import com.shop.microservices.product.Dto.ProductRequestDTO;
import com.shop.microservices.product.Exception.EntityCreationException;
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
    public ResponseEntity<?> createProduct(@RequestBody ProductRequestDTO productRequest) throws EntityCreationException {
            // Call the service layer to create a product and return a successful response
            return ResponseEntity.ok().body(productService.createProduct(productRequest));
    }

}
