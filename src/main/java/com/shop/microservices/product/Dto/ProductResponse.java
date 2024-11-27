package com.shop.microservices.product.Dto;

import java.math.BigDecimal;

public record ProductResponse(int id, String name, String description, BigDecimal price) {
}
