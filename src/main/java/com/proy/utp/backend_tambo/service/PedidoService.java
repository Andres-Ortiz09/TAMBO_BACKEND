package com.proy.utp.backend_tambo.service;

import com.proy.utp.backend_tambo.model.Pedido;
import com.proy.utp.backend_tambo.model.EstadoPedido;
import com.proy.utp.backend_tambo.model.Product;
import com.proy.utp.backend_tambo.model.User;
import com.proy.utp.backend_tambo.repository.PedidoRepository;
import com.proy.utp.backend_tambo.repository.ProductRepository;
import com.proy.utp.backend_tambo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Pedido crearPedido(String email, List<Long> productosIds) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<Product> productos = productRepository.findAllById(productosIds);
        if (productos.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron productos");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setProductos(productos);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido cancelarPedido(Long pedidoId, String email) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Solo el dueño del pedido puede cancelarlo
        if (!pedido.getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalStateException("No autorizado para cancelar este pedido");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden cancelar pedidos pendientes");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
    }
}