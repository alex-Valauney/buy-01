package com.buy01.product.controller;

import com.buy01.product.config.JwtUtils;
import com.buy01.product.dto.ProductRequest;
import com.buy01.product.exception.ForbiddenException;
import com.buy01.product.exception.UnauthorizedException;
import com.buy01.product.model.Product;
import com.buy01.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtUtils jwtUtils;

    // Public: Voir tous les produits
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Seller Only: Créer un produit
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody ProductRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // 1. Validation manuelle simple (Role Check)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing Token");
        }
        String token = authHeader.substring(7);

        // 2. Extraire les infos du token
        String userRole = jwtUtils.extractRole(token);
        String userId = jwtUtils.extractId(token);

        if (!"SELLER".equals(userRole)) {
            throw new ForbiddenException("Access Denied: Sellers only");
        }

        return ResponseEntity.ok(productService.createProduct(request, userId));
    }

    // Seller Only: Voir mes produits
    @GetMapping("/my-products")
    public ResponseEntity<List<Product>> getMyProducts(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing Token");
        }
        String token = authHeader.substring(7);
        String userId = jwtUtils.extractId(token);

        return ResponseEntity.ok(productService.getMyProducts(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable String id,
            @RequestBody ProductRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);
        validateSeller(token);
        String userId = jwtUtils.extractId(token);

        return ResponseEntity.ok(productService.updateProduct(id, request, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);
        validateSeller(token);
        String userId = jwtUtils.extractId(token);

        productService.deleteProduct(id, userId);
        return ResponseEntity.noContent().build();
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing Token");
        }
        return authHeader.substring(7);
    }

    private void validateSeller(String token) {
        String userRole = jwtUtils.extractRole(token);
        if (!"SELLER".equals(userRole)) {
            throw new ForbiddenException("Access Denied: Sellers only");
        }
    }
}
