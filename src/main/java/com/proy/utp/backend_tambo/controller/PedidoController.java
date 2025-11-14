package com.proy.utp.backend_tambo.controller;

import com.proy.utp.backend_tambo.dto.CrearPedidoRequest;
import com.proy.utp.backend_tambo.dto.PedidoResponseDTO;
import com.proy.utp.backend_tambo.model.Pedido;
import com.proy.utp.backend_tambo.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(
            @Valid @RequestBody CrearPedidoRequest request,
            Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pedido pedido = pedidoService.crearPedido(
                authentication.getName(),
                request.getProductosIds(),
                request.getCantidades(),
                request.getEstado(),
                request.getFecha()
        );

        PedidoResponseDTO dto = pedidoService.convertirPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/{pedidoId}")
    public ResponseEntity<PedidoResponseDTO> editarPedidoComoAdmin(
            @PathVariable Long pedidoId,
            @Valid @RequestBody CrearPedidoRequest request) {

        Pedido pedido = pedidoService.editarPedidoComoAdmin(
                pedidoId,
                request.getProductosIds(),
                request.getCantidades(),
                request.getEstado(),
                request.getFecha()
        );

        PedidoResponseDTO dto = pedidoService.convertirPedido(pedido);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{pedidoId}/cancelar")
    public ResponseEntity<Pedido> cancelarPedido(
            @PathVariable Long pedidoId,
            Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pedido pedidoCancelado = pedidoService.cancelarPedido(pedidoId, authentication.getName());
        return ResponseEntity.ok(pedidoCancelado);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/todos")
    public ResponseEntity<List<Pedido>> obtenerTodosLosPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<Pedido>> obtenerPedidosDelUsuario(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuario(authentication.getName());
        return ResponseEntity.ok(pedidos);
    }
}
