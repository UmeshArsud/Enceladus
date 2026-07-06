package com.enceladus.enceladus.service.products_service;

import com.enceladus.enceladus.Exeption.ProductNotFoundException;
import com.enceladus.enceladus.dto.products_dto.ProductRequest;
import com.enceladus.enceladus.model.products.Product;
import com.enceladus.enceladus.repo.products_repo.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Products_service {

    @Autowired
    private ProductsRepo repo;

    public List<Product> showProducts() {
        return repo.findAll();
    }

    public Optional<Product> showProductsById(int id) {
        return repo.findById(id);
    }

    public Product addProduct(ProductRequest request) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductPrice(request.getProductPrice());
        product.setProductQuantity(request.getProductQuantity());
        return repo.save(product);
    }

    // id comes from the path, never from the request body — prevents a caller
    // from overwriting a different record than the one in the URL
    public Product updateProduct(int id, ProductRequest request) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        existing.setProductName(request.getProductName());
        existing.setProductPrice(request.getProductPrice());
        existing.setProductQuantity(request.getProductQuantity());
        return repo.save(existing);
    }

    public void deleteProductById(int id) {
        if (!repo.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        repo.deleteById(id);
    }
}
