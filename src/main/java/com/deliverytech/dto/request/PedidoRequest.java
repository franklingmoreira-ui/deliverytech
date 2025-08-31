package com.deliverytech.dto.request;

import com.deliverytech.entity.Endereco; // Importa a classe Endereco
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

/**
 * DTO para dados de entrada de um Pedido.
 */
@Data
@Schema(description = "DTO de entrada para criar um Pedido.")
public class PedidoRequest {

    @NotNull(message = "O ID do cliente é obrigatório")
    @Schema(description = "ID do cliente que está fazendo o pedido", example = "1", required = true)
    private Long clienteId;

    @NotNull(message = "O ID do restaurante é obrigatório")
    @Schema(description = "ID do restaurante onde o pedido será realizado", example = "2", required = true)
    private Long restauranteId;

    // CORREÇÃO AQUI: O tipo foi alterado para Endereco
    @NotNull(message = "O endereço de entrega é obrigatório")
    @Valid // Garante que os campos dentro do objeto Endereco também sejam validados
    @Schema(description = "Objeto com os dados do endereço de entrega do pedido", required = true)
    private Endereco enderecoEntrega;

    @NotBlank(message = "A forma de pagamento é obrigatória")
    @Schema(description = "Forma de pagamento do pedido", example = "PIX", required = true)
    private String formaPagamento;

    @Schema(description = "Observações gerais do pedido", example = "Deixar na portaria")
    private String observacao;

    @Valid
    @NotNull(message = "A lista de itens não pode ser nula")
    @Size(min = 1, message = "O pedido deve conter pelo menos 1 item")
    @Schema(description = "Lista de itens do pedido", required = true)
    private List<ItemPedidoRequest> itens; // Renomeado para consistência

    /**
     * Classe aninhada para os itens do pedido na requisição.
     */
    @Data
    @Schema(description = "DTO representando um item do pedido na requisição")
    public static class ItemPedidoRequest {

        @NotNull(message = "O ID do produto é obrigatório")
        @Schema(description = "ID do produto", example = "10", required = true)
        private Long produtoId;

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade mínima é 1")
        @Schema(description = "Quantidade do produto", example = "2", required = true)
        private Integer quantidade;

        @Schema(description = "Observação específica para o item", example = "Sem cebola")
        private String observacao;
    }
}
