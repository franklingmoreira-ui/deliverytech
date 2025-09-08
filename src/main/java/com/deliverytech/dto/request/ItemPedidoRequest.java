// Arquivo: src/main/java/com/deliverytech/dto/ItemPedidoDTO.java
package com.deliverytech.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Representa um Objeto de Transferência de Dados (DTO) para o Item do Pedido.
 * Usado para transferir dados entre a camada de serviço e a camada de controle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoRequest {
    private Long produtoId;
    private Integer quantidade;
    private Double precoUnitario;
}
