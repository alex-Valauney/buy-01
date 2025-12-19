package com.buy01.product.service;

import com.buy01.product.dto.ProductRequest;
import com.buy01.product.model.Product;
import com.buy01.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product createProduct(ProductRequest request, String sellerId) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .imageUrls(request.getImageUrls())
                .sellerId(sellerId) // Link to the user who created it
                .build();
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public List<Product> getMyProducts(String sellerId) {
        return repository.findBySellerId(sellerId);
    }

    public Product updateProduct(String id, ProductRequest request, String sellerId) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSellerId().equals(sellerId)) {
            throw new RuntimeException("Access Denied: You are not the owner of this product");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrls(request.getImageUrls());

        return repository.save(product);
    }

    public void deleteProduct(String id, String sellerId) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSellerId().equals(sellerId)) {
            throw new RuntimeException("Access Denied: You are not the owner of this product");
        }

        repository.delete(product);
    }
}
