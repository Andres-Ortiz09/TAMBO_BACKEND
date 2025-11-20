package com.proy.utp.backend_tambo.controller;

import com.proy.utp.backend_tambo.dto.CrearPedidoRequest;
import com.proy.utp.backend_tambo.dto.PedidoResponseDTO;
import com.proy.utp.backend_tambo.model.Pedido;
import com.proy.utp.backend_tambo.service.PedidoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
                request.getCantidades()
        );

        PedidoResponseDTO dto = pedidoService.convertirPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
