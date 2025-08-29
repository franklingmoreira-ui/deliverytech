package com.deliverytech.dto.request;

import com.deliverytech.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Representa um DTO de requisição para registrar um novo usuário.
 * Contém informações necessárias para a autenticação e autorização.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "{campo.email.obrigatorio}")
    @Email(message = "{campo.email.invalido}")
    @Schema(description = "Endereço de e-mail do usuário", example = "usuario@email.com")
    private String email;

    @NotBlank(message = "{campo.senha.obrigatorio}")
    @Length(min = 6, message = "{campo.senha.minimo}")
    @Schema(description = "Senha do usuário", example = "senha123")
    private String senha;

    @NotBlank(message = "{campo.nome.obrigatorio}")
    @Schema(description = "Nome do usuário", example = "João da Silva")
    private String nome;

    @Schema(description = "Função do usuário", example = "CLIENTE")
    private Role role;

    // Adicionado para compatibilidade com o AuthController
    @Schema(description = "ID do restaurante, obrigatório se a role for RESTAURANTE", example = "1")
    private Long restauranteId;
}
