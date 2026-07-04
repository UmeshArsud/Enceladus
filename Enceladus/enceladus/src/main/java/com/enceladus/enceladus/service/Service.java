package com.enceladus.enceladus.service;

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

    public void updateProduct(Product product) {
        repo.save(product);
    }

    public void addProduct(Product product) {
        repo.save(product);
    }

    public void deleteProductById(int id) {
        repo.deleteById(id);
    }
}
