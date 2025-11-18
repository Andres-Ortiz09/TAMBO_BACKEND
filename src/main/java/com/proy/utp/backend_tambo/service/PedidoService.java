package com.proy.utp.backend_tambo.service;

import com.proy.utp.backend_tambo.dto.PedidoResponseDTO;
import com.proy.utp.backend_tambo.model.*;
import com.proy.utp.backend_tambo.repository.PedidoRepository;
import com.proy.utp.backend_tambo.repository.ProductRepository;
import com.proy.utp.backend_tambo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Pedido crearPedido(String email,
            List<Long> productosIds,
            List<Integer> cantidades,
            String estado,
            String fecha) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (productosIds.size() != cantidades.size()) {
            throw new IllegalArgumentException("Cantidad de productos y cantidades no coinciden");
        }

        Pedido pedido = new Pedido();
        pedido.setUser(user);

        // convertir fecha (String → LocalDateTime)
        if (fecha != null && !fecha.isEmpty()) {
            LocalDate localDate = LocalDate.parse(fecha); // formato yyyy-MM-dd
            pedido.setFechaPedido(localDate.atStartOfDay());
        } else {
            pedido.setFechaPedido(LocalDateTime.now());
        }

        // asignar estado
        if (estado != null && !estado.isEmpty()) {
            try {
                pedido.setEstado(EstadoPedido.valueOf(estado.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado inválido: " + estado);
            }
        } else {
            pedido.setEstado(EstadoPedido.PENDIENTE);
        }

        List<PedidoItem> items = new ArrayList<>();
        for (int i = 0; i < productosIds.size(); i++) {
            Product producto = productRepository.findById(productosIds.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            Integer cantidad = cantidades.get(i);

            // validación de cantidad
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
            }
            if (producto.getStock() < cantidad) {
                throw new IllegalArgumentException("La cantidad supera el stock disponible");
            }

            BigDecimal precioUnitario = BigDecimal.valueOf(producto.getPrice());
            BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            PedidoItem item = new PedidoItem();
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setPrecioUnitario(precioUnitario);
            item.setTotal(total);
            item.setPedido(pedido);

            items.add(item);

            // descontar stock
            producto.setStock(producto.getStock() - cantidad);
            productRepository.save(producto);
        }

        pedido.setItems(items);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido editarPedidoComoAdmin(Long pedidoId,
            List<Long> productosIds,
            List<Integer> cantidades,
            String estado,
            String fecha) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        // Actualizar fecha
        if (fecha != null && !fecha.isEmpty()) {
            LocalDate localDate = LocalDate.parse(fecha);
            pedido.setFechaPedido(localDate.atStartOfDay());
        }

        // Actualizar estado
        if (estado != null && !estado.isEmpty()) {
            try {
                pedido.setEstado(EstadoPedido.valueOf(estado.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado inválido: " + estado);
            }
        }

        if (productosIds.size() != cantidades.size()) {
            throw new IllegalArgumentException("Cantidad de productos y cantidades no coinciden");
        }

        // Devolver stock de los items anteriores
        for (PedidoItem item : pedido.getItems()) {
            Product producto = item.getProducto();
            producto.setStock(producto.getStock() + item.getCantidad());
            productRepository.save(producto);
        }

        List<PedidoItem> nuevosItems = new ArrayList<>();
        for (int i = 0; i < productosIds.size(); i++) {
            Product producto = productRepository.findById(productosIds.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            Integer cantidad = cantidades.get(i);

            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
            }
            if (producto.getStock() < cantidad) {
                throw new IllegalArgumentException("La cantidad supera el stock disponible");
            }

            BigDecimal precioUnitario = BigDecimal.valueOf(producto.getPrice());
            BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            PedidoItem item = new PedidoItem();
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setPrecioUnitario(precioUnitario);
            item.setTotal(total);
            item.setPedido(pedido);

            nuevosItems.add(item);

            // descontar stock
            producto.setStock(producto.getStock() - cantidad);
            productRepository.save(producto);
        }

        pedido.setItems(nuevosItems);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido editarPedido(Long pedidoId,
            String email,
            List<Long> productosIds,
            List<Integer> cantidades,
            String estado,
            String fecha) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        if (!pedido.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("No autorizado para editar este pedido");
        }

        // Actualizar fecha
        if (fecha != null && !fecha.isEmpty()) {
            LocalDate localDate = LocalDate.parse(fecha);
            pedido.setFechaPedido(localDate.atStartOfDay());
        }

        // Actualizar estado
        if (estado != null && !estado.isEmpty()) {
            try {
                pedido.setEstado(EstadoPedido.valueOf(estado.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado inválido: " + estado);
            }
        }

        if (productosIds.size() != cantidades.size()) {
            throw new IllegalArgumentException("Cantidad de productos y cantidades no coinciden");
        }

        // Devolver stock de los items anteriores
        for (PedidoItem item : pedido.getItems()) {
            Product producto = item.getProducto();
            producto.setStock(producto.getStock() + item.getCantidad());
            productRepository.save(producto);
        }

        List<PedidoItem> nuevosItems = new ArrayList<>();
        for (int i = 0; i < productosIds.size(); i++) {
            Product producto = productRepository.findById(productosIds.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            Integer cantidad = cantidades.get(i);

            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
            }
            if (producto.getStock() < cantidad) {
                throw new IllegalArgumentException("La cantidad supera el stock disponible");
            }

            BigDecimal precioUnitario = BigDecimal.valueOf(producto.getPrice());
            BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            PedidoItem item = new PedidoItem();
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setPrecioUnitario(precioUnitario);
            item.setTotal(total);
            item.setPedido(pedido);

            nuevosItems.add(item);

            // descontar stock
            producto.setStock(producto.getStock() - cantidad);
            productRepository.save(producto);
        }

        pedido.setItems(nuevosItems);
        pedido.setUser(user);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido editarEstadoPedido(Long pedidoId, String estado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        // Validar el estado
        try {
            EstadoPedido estadoPedido = EstadoPedido.valueOf(estado.toUpperCase());
            pedido.setEstado(estadoPedido);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + estado);
        }

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido cancelarPedido(Long pedidoId, String email) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!pedido.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("No autorizado para cancelar este pedido");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden cancelar pedidos pendientes");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void eliminarPedido(Long pedidoId) {
        // Buscar el pedido por ID
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        // Devolver el stock de los productos que fueron parte del pedido eliminado
        for (PedidoItem item : pedido.getItems()) {
            Product producto = item.getProducto();
            producto.setStock(producto.getStock() + item.getCantidad());
            productRepository.save(producto);  // Guardar el cambio de stock
        }

        // Eliminar el pedido de la base de datos
        pedidoRepository.delete(pedido);
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
    }

    public PedidoResponseDTO convertirPedido(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setIdPedido(pedido.getId());
        dto.setEstado(pedido.getEstado().name());
        dto.setFecha(pedido.getFechaPedido());

        List<PedidoResponseDTO.ItemDTO> itemDTOs = new ArrayList<>();
        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (PedidoItem item : pedido.getItems()) {
            PedidoResponseDTO.ItemDTO i = new PedidoResponseDTO.ItemDTO();
            i.setNombreProducto(item.getProducto().getName());
            i.setCantidad(item.getCantidad());
            i.setPrecioUnitario(item.getPrecioUnitario());
            i.setTotal(item.getTotal());
            itemDTOs.add(i);
            totalGeneral = totalGeneral.add(item.getTotal());
        }

        dto.setItems(itemDTOs);
        dto.setTotalGeneral(totalGeneral);
        return dto;
    }

    public List<Pedido> obtenerPedidosPorUsuario(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return pedidoRepository.findByUser(user);
    }
}
