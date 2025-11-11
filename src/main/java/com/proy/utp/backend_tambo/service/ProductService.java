package com.proy.utp.backend_tambo.service;

import com.proy.utp.backend_tambo.model.Product;
import com.proy.utp.backend_tambo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Product create(Product product) {
        return repository.save(product);
    }

    public Product update(Long id, Product updatedProduct) {
        return repository.findById(id)
                .map(p -> {
                    p.setName(updatedProduct.getName());
                    p.setPrice(updatedProduct.getPrice());
                    p.setStock(updatedProduct.getStock());
                    p.setCategory(updatedProduct.getCategory());
                    p.setImage(updatedProduct.getImage());
                    return repository.save(p);
                })
                .orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}