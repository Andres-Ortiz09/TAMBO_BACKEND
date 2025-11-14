package com.proy.utp.backend_tambo.repository;

import com.proy.utp.backend_tambo.model.Pedido;
import com.proy.utp.backend_tambo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUser(User user);
}