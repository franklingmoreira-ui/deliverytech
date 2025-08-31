package com.deliverytech.service;

import com.deliverytech.entity.Pedido;
import com.deliverytech.entity.StatusPedido;

import java.util.List;

public interface PedidoService {
    Pedido criar(Pedido pedido);
    // CORREÇÃO AQUI: Retorna a entidade diretamente
    Pedido buscarPorId(Long id);
    List<Pedido> listarPorCliente(Long clienteId);
    List<Pedido> listarPorRestaurante(Long restauranteId);
    Pedido atualizarStatus(Long id, StatusPedido status);
    void cancelar(Long id);
}
