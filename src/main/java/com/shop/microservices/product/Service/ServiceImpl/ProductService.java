package com.shop.microservices.product.Service.ServiceImpl;

import com.shop.microservices.product.Dto.ProductRequest;
import com.shop.microservices.product.Dto.ProductResponse;
import com.shop.microservices.product.Repository.ProductRepository;
import com.shop.microservices.product.Service.ServiceInterface.IProductService;
import com.shop.microservices.product.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Override
    public Product createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();

        Product savedProduct =  productRepository.save(product);
        log.info("Product created successfully");
        return savedProduct;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice()))
                .toList();
    }
}
