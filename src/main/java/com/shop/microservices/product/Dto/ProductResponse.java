package com.shop.microservices.product.Dto;

import java.math.BigDecimal;

public record ProductResponse(java.util.UUID id, String name, String description, BigDecimal price) {
}
