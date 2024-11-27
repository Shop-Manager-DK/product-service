package com.shop.microservices.product.Service.ServiceInterface;

import com.shop.microservices.product.Dto.ProductRequest;
import com.shop.microservices.product.Dto.ProductResponse;
import com.shop.microservices.product.model.Product;

import java.util.List;

public interface IProductService {
    public Product createProduct(ProductRequest productRequest);
    public List<ProductResponse> getAllProducts();
}
