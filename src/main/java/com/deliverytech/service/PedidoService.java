package com.deliverytech.service;

import com.deliverytech.entity.Pedido;
import com.deliverytech.entity.StatusPedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {
    Pedido criar(Pedido pedido);
    Optional<Pedido> buscarPorId(Long id);
    List<Pedido> listarPorCliente(Long clienteId);
    List<Pedido> listarPorRestaurante(Long restauranteId);
    Pedido atualizarStatus(Long id, StatusPedido status);
    void cancelar(Long id);
}