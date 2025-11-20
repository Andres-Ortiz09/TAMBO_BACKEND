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
import java.time.ZoneId;
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
    public Pedido crearPedido(String email, List<Long> productosIds, List<Integer> cantidades) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (productosIds.size() != cantidades.size()) {
            throw new IllegalArgumentException("Cantidad de productos y cantidades no coinciden");
        }

        Pedido pedido = new Pedido();
        pedido.setUser(user);

        pedido.setFechaPedido(LocalDate.now(ZoneId.of("America/Lima")));

        List<PedidoItem> items = new ArrayList<>();

        for (int i = 0; i < productosIds.size(); i++) {
            Product producto = productRepository.findById(productosIds.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            Integer cantidad = cantidades.get(i);

            if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
            if (producto.getStock() < cantidad) throw new IllegalArgumentException("La cantidad supera el stock disponible");

            BigDecimal precioUnitario = BigDecimal.valueOf(producto.getPrice());
            BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            PedidoItem item = new PedidoItem();
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setPrecioUnitario(precioUnitario);
            item.setTotal(total);
            item.setPedido(pedido);

            items.add(item);

            // Descontar stock
            producto.setStock(producto.getStock() - cantidad);
            productRepository.save(producto);
        }

        pedido.setItems(items);
        return pedidoRepository.save(pedido);
    }

    public PedidoResponseDTO convertirPedido(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setIdPedido(pedido.getId());
        dto.setFecha(pedido.getFechaPedido());

        List<PedidoResponseDTO.ItemDTO> itemDTOs = new ArrayList<>();
        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (PedidoItem item : pedido.getItems()) {
            PedidoResponseDTO.ItemDTO it = new PedidoResponseDTO.ItemDTO();
            it.setNombreProducto(item.getProducto().getName());
            it.setCantidad(item.getCantidad());
            it.setPrecioUnitario(item.getPrecioUnitario());
            it.setTotal(item.getTotal());

            itemDTOs.add(it);
            totalGeneral = totalGeneral.add(item.getTotal());
        }

        dto.setItems(itemDTOs);
        dto.setTotalGeneral(totalGeneral);
        return dto;
    }
}
