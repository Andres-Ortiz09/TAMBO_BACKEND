package com.proy.utp.backend_tambo.service;

import com.proy.utp.backend_tambo.model.Product;
import com.proy.utp.backend_tambo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product create(Product p) {
        return repo.save(p);
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Product findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Product update(Long id, Product p) {
        Product prod = findById(id);
        prod.setName(p.getName());
        prod.setDescription(p.getDescription());
        prod.setPrice(p.getPrice());
        prod.setStock(p.getStock());
        prod.setCategory(p.getCategory());
        prod.setImage(p.getImage());
        return repo.save(prod);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
