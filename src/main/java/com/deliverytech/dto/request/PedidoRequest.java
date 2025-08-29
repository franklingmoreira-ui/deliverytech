package com.deliverytech.dto.request;

import com.deliverytech.entity.Endereco; // Import adicionado
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

/**
 * DTO para dados de entrada de um Pedido.
 * Usa Lombok (@Data) para gerar getters, setters, toString(),
 * equals() e hashCode() automaticamente, mantendo o código limpo.
 */
@Data
@Schema(description = "DTO de entrada para criar/atualizar um Pedido. Preços/valores são calculados no servidor.")
public class PedidoRequest {

    // Anotação para garantir que o campo não seja nulo.
    @NotNull(message = "clienteId é obrigatório")
    @Schema(description = "ID do cliente que está fazendo o pedido", example = "1", required = true)
    private Long clienteId;

    // Anotação para garantir que o campo não seja nulo.
    @NotNull(message = "restauranteId é obrigatório")
    @Schema(description = "ID do restaurante onde o pedido será realizado", example = "2", required = true)
    private Long restauranteId;

    // Corrigido para usar o objeto Endereco
    @NotNull(message = "enderecoEntrega é obrigatório")
    @Schema(description = "Endereço de entrega do pedido", required = true)
    private Endereco enderecoEntrega;

    // Anotação para garantir que a forma de pagamento não seja nula ou vazia.
    @NotBlank(message = "formaPagamento é obrigatória")
    @Schema(description = "Forma de pagamento do pedido", example = "PIX", required = true)
    private String formaPagamento;

    // Campo opcional, não precisa de anotação de validação.
    @Schema(description = "Observações gerais do pedido", example = "Deixar na portaria")
    private String observacao;

    /**
     * @Valid é crucial aqui! Ele garante que o validador
     * "mergulhe" na lista e valide cada ItemDTO individualmente.
     * @NotNull garante que a lista em si não seja nula.
     * @Size garante que a lista tenha pelo menos um item.
     */
    @Valid
    @NotNull(message = "itens não pode ser nulo")
    @Size(min = 1, message = "o pedido deve conter pelo menos 1 item")
    @Schema(description = "Lista de itens do pedido", required = true)
    private List<ItemPedidoRequest> itens;

    /**
     * Classe aninhada estática para representar um item do pedido.
     * O uso de @Data do Lombok aqui também economiza a necessidade de getters e setters.
     */
    @Data
    @Schema(description = "DTO representando um item do pedido")
    public static class ItemPedidoRequest {

        @NotNull(message = "produtoId é obrigatório")
        @Schema(description = "ID do produto", example = "10", required = true)
        private Long produtoId;

        @NotNull(message = "quantidade é obrigatória")
        @Min(value = 1, message = "quantidade mínima é 1")
        @Schema(description = "Quantidade do produto", example = "2", required = true)
        private Integer quantidade;

        @Schema(description = "Observação específica para o item", example = "Sem cebola")
        private String observacao;
    }
}
