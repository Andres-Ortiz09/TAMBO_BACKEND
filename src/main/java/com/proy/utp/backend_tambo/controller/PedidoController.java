package com.proy.utp.backend_tambo.controller;

import com.proy.utp.backend_tambo.dto.CrearPedidoRequest;
import com.proy.utp.backend_tambo.dto.PedidoResponseDTO;
import com.proy.utp.backend_tambo.model.Pedido;
import com.proy.utp.backend_tambo.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "API para gestionar pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Crear un nuevo pedido", description = "Crea un pedido para el usuario autenticado con los productos y cantidades especificadas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido creado correctamente"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
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
        );

        PedidoResponseDTO dto = pedidoService.convertirPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
