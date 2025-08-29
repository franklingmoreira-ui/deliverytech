package com.deliverytech.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO usado para receber dados do cliente (entrada da API)
 */
@Data
@Schema(description = "DTO de entrada para cadastro ou atualização de cliente")
public class ClienteRequest {

    @Schema(description = "Nome completo do cliente", example = "João Silva", required = true)
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Email do cliente", example = "joao.silva@email.com", required = true)
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Telefone do cliente", example = "11999999999", required = true)
    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 8, max = 20, message = "Telefone deve ter entre 8 e 20 caracteres")
    private String telefone;

    @Schema(description = "Endereço completo do cliente", example = "Rua das Flores, 123 - Centro", required = true)
    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;
}
