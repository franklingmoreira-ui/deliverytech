package com.deliverytech.repository;

import com.deliverytech.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Método para buscar por ID do cliente (nome corrigido)
    List<Pedido> findByClienteId(Long clienteId);

    // Método para buscar por ID do restaurante
    List<Pedido> findByRestauranteId(Long restauranteId);
}
