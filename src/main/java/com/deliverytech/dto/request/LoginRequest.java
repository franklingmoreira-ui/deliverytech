package com.deliverytech.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Email(message = "O email deve ser válido")
    @NotBlank(message = "O email é obrigatório")
    @Schema(description = "Endereço de e-mail do usuário", example = "usuario@email.com", required = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "senha123", required = true)
    private String senha;
}
