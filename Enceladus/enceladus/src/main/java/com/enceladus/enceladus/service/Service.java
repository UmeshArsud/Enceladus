package com.enceladus.enceladus.service;

import com.enceladus.enceladus.Exeption.ProductNotFoundException;
import com.enceladus.enceladus.dto.ProductRequest;
import com.enceladus.enceladus.model.Product;
import com.enceladus.enceladus.repo.Repo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private Repo repo;

    public List<Product> showProducts() {
        return repo.findAll();
    }

    public Optional<Product> showProductsById(int id) {
        return repo.findById(id);
    }

    public Product addProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setAge(request.getAge());
        product.setMarks(request.getMarks());
        return repo.save(product);
    }

    // id comes from the path, never from the request body — prevents a caller
    // from overwriting a different record than the one in the URL
    public Product updateProduct(int id, ProductRequest request) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        existing.setName(request.getName());
        existing.setAge(request.getAge());
        existing.setMarks(request.getMarks());
        return repo.save(existing);
    }

    public void deleteProductById(int id) {
        if (!repo.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        repo.deleteById(id);
    }
}
