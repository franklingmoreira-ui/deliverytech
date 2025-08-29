package com.deliverytech.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoRequest {
    private String nome;
    private String descricao;
    private String categoria;
    private BigDecimal preco;
    private Long restauranteId;
}
