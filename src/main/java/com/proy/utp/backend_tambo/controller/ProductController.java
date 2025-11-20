package com.proy.utp.backend_tambo.controller;

import com.proy.utp.backend_tambo.model.Product;
import com.proy.utp.backend_tambo.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Productos", description = "API para gestionar productos")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todos los productos", description = "Retorna la lista completa de productos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Product>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener un producto por ID", description = "Retorna un producto específico según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear un producto", description = "Crea un nuevo producto (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto creado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> create(@Valid @RequestBody Product p) {
        Product saved = service.create(p);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Actualizar un producto", description = "Actualiza un producto existente (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product product) {
        return ResponseEntity.ok(service.update(id, product));
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto por ID (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
