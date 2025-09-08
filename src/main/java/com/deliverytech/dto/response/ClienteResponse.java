package com.deliverytech.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado para enviar dados do cliente (resposta da API).
 * Usa anotações do Lombok para gerar automaticamente construtores e métodos.
 */
@Data // Gera Getters, Setters, toString(), equals() e hashCode()
@NoArgsConstructor // Gera um construtor sem argumentos
@AllArgsConstructor // Gera um construtor com todos os campos
@Schema(description = "DTO de resposta contendo informações do cliente")
public class ClienteResponse {

    @Schema(description = "ID do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "João Silva")
    private String nome;

    @Schema(description = "E-mail do cliente", example = "joao.silva@email.com")
    private String email;

    @Schema(description = "Telefone do cliente", example = "11999999999")
    private String telefone;

    @Schema(description = "Endereço completo do cliente", example = "Rua das Flores, 123 - Centro")
    private String endereco;

    @Schema(description = "Indica se o cliente está ativo", example = "true")
    private boolean ativo;
}
