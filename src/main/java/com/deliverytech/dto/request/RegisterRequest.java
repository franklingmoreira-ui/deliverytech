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

@NotBlank(message = "O email é obrigatório")
@Email(message = "O email deve ser válido")
@Schema(description = "Endereço de e-mail do usuário", example = "usuario@email.com")
private String email;

@NotBlank(message = "A senha é obrigatória")
@Length(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
@Schema(description = "Senha do usuário", example = "senha123")
private String senha;

@NotBlank(message = "O nome é obrigatório")
@Schema(description = "Nome do usuário", example = "João da Silva") private String nome;

@Schema(description = "Função do usuário (CLIENTE, RESTAURANTE, ADMIN, ENTREGADOR)", example = "CLIENTE")
private Role role;

 // Adicionado para compatibilidade com o AuthController
@Schema(description = "ID do restaurante (obrigatório apenas se a role for RESTAURANTE)", example = "1")
private Long restauranteId;
}
