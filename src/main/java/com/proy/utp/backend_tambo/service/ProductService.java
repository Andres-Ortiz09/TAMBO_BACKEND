package com.proy.utp.backend_tambo.service;

import com.proy.utp.backend_tambo.model.Product;
import com.proy.utp.backend_tambo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Product create(Product product) {
        return repository.save(product);
    }

    // Método para buscar producto por id (lanza excepción si no existe)
    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Product update(Long id, Product product) {
        Product existing = findById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setCategory(product.getCategory());
        if (product.getImage() != null) {
            existing.setImage(product.getImage());
        }
        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
