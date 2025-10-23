package com.proy.utp.backend_tambo.repository;

import com.proy.utp.backend_tambo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
