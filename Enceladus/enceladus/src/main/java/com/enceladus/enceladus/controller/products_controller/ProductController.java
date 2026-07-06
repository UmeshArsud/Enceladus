package com.enceladus.enceladus.controller.products_controller;

import com.enceladus.enceladus.dto.products_dto.ProductRequest;
import com.enceladus.enceladus.model.products.Product;
import com.enceladus.enceladus.service.products_service.Products_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private Products_service service;

    // any authenticated user can read (already enforced by SecurityConfig's anyRequest().authenticated())
    @GetMapping
    public List<Product> showProduct() {
        return service.showProducts();
    }

    @GetMapping("/{productId}")
    public Optional<Product> showProductById(@PathVariable int productId) {
        return service.showProductsById(productId);
    }

    // write operations restricted to ADMIN at the SecurityConfig layer;
    // @PreAuthorize repeats the check here as defense-in-depth in case the
    // URL-based rule is ever changed without updating this controller
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(service.addProduct(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable int productId, @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(service.updateProduct(productId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable int productId) {
        service.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }
}
