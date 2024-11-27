package com.shop.microservices.product.Dto;

import java.math.BigDecimal;

public record ProductRequest(String name, String description, BigDecimal price) {
}
