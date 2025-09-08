package com.deliverytech.entity;

public enum StatusPedido {
    CRIADO,          // O pedido foi criado pelo cliente, mas ainda não foi confirmado pelo restaurante.
    CONFIRMADO,      // O restaurante aceitou o pedido.
    EM_PREPARO,      // O pedido está sendo preparado.
    SAIU_PARA_ENTREGA, // O pedido está a caminho do cliente.
    ENTREGUE,        // O pedido foi entregue com sucesso.
    CANCELADO        // O pedido foi cancelado.
}
