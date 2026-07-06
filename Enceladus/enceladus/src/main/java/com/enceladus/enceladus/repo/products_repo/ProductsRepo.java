package com.enceladus.enceladus.repo.products_repo;

import com.enceladus.enceladus.model.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepo extends JpaRepository<Product, Integer> {
}
