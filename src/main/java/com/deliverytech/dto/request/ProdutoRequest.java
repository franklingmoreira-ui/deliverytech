package com.deliverytech.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoRequest {

    @NotNull(message = "O ID do restaurante é obrigatório")
    private Long restauranteId;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 2, max = 50, message = "O nome do produto deve ter entre 2 e 50 caracteres")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, message = "A descrição deve ter no mínimo 10 caracteres")
    private String descricao;

    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    @Max(value = 500, message = "O preço não pode exceder R$ 500,00")
    private BigDecimal preco;
}
