package com.proy.utp.backend_tambo.controller;

import com.proy.utp.backend_tambo.dto.CrearPedidoRequest;
import com.proy.utp.backend_tambo.model.Pedido;
import com.proy.utp.backend_tambo.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(
            @RequestBody CrearPedidoRequest request,
            Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        System.out.println("Usuario autenticado: " + authentication.getName());
        System.out.println("Autoridades: " + authentication.getAuthorities());
        
        Pedido pedido = pedidoService.crearPedido(
            authentication.getName(),
            request.getProductosIds()
        );
        return ResponseEntity.ok(pedido);
    }

    @PostMapping("/{pedidoId}/cancelar")
    public ResponseEntity<Pedido> cancelarPedido(
            @PathVariable Long pedidoId,
            Authentication authentication) {
        Pedido pedidoCancelado = pedidoService.cancelarPedido(pedidoId, authentication.getName());
        return ResponseEntity.ok(pedidoCancelado);
    }

    @GetMapping("/admin/todos")
    public ResponseEntity<List<Pedido>> obtenerTodosLosPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }
}