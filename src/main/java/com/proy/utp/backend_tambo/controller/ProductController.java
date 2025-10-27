package com.proy.utp.backend_tambo.controller;

import com.proy.utp.backend_tambo.model.Product;
import com.proy.utp.backend_tambo.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    //  Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Product>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    //  Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Crear producto con imagen (opcional)
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("stock") Integer stock,
            @RequestParam("category") String category,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory(category);

            if (image != null && !image.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path path = Paths.get("uploads").resolve(filename).toAbsolutePath();
                Files.createDirectories(path.getParent());
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                product.setImage(filename);
            }

            Product saved = service.create(product);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la imagen: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el producto: " + e.getMessage());
        }
    }

    // 🔹 Actualizar producto con imagen opcional
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("stock") Integer stock,
            @RequestParam("category") String category,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            Product existing = service.findById(id);
            existing.setName(name);
            existing.setDescription(description);
            existing.setPrice(price);
            existing.setStock(stock);
            existing.setCategory(category);

            // 📸 Si se sube una nueva imagen, reemplazar
            if (image != null && !image.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path path = Paths.get("uploads/" + filename);
                Files.createDirectories(path.getParent());
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                existing.setImage(filename);
            }

            Product updated = service.update(id, existing);
            return ResponseEntity.ok(updated);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la imagen: " + e.getMessage());
        }
    }

    //  Eliminar producto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
