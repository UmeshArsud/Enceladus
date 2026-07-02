package com.enceladus.enceladus.controller;

import com.enceladus.enceladus.model.Product;
import com.enceladus.enceladus.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    private Service service;
    private Product prod;

    @GetMapping("/products")
    public List<Product> showProduct(){
        return service.showProducts();
    }

    @GetMapping("/products/{productId}")
    public Optional<Product> showProductById(@PathVariable int productId){
        return service.showProductsById(productId);
    }

    @PostMapping("/products")
    public void addProduct(@RequestBody Product prod){
        service.addProduct(prod);
    }

    @PutMapping("/products")
    public void updateProduct(@RequestBody Product prod){
        service.updateProduct(prod);
    }

    @DeleteMapping("/products/{productId}")
    public void deleteProductById(@PathVariable int productId){
        service.deleteProductById(productId);
    }
}