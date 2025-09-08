package com.deliverytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor // <-- O construtor que o controller precisa
public class RestauranteResponse {
    private Long id;
    private String nome;
    private String categoria;
    private String endereco;
    private String telefone;
    private BigDecimal taxaEntrega;
    private Integer tempoEntrega;
    private String horarioFuncionamento;
    private boolean ativo;
}
