package com.buy01.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;

    // ID du vendeur (provenant du User Service)
    private String sellerId;

    // Liste des URLs d'images (provenant du Media Service)
    private List<String> imageUrls;
}
