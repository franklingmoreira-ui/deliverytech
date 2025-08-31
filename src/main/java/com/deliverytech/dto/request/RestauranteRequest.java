package com.deliverytech.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

/**
* DTO para dados de cadastro e atualização de restaurante.
*/
@Data // Gera getters, setters, etc. automaticamente
@Schema(description = "Dados para cadastro e atualização de restaurante")
public class RestauranteRequest {

    @Schema(description = "Nome do restaurante", example = "Pizza Express", required = true)
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Categoria do restaurante", example = "Italiana",
            allowableValues = {"Italiana", "Brasileira", "Japonesa", "Mexicana", "Árabe"})
    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    @Schema(description = "Endereço completo do restaurante", example = "Rua das Flores, 123 - Centro")
    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String endereco;

    @Schema(description = "Telefone para contato", example = "11999999999")
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String telefone;

    @Schema(description = "Taxa de entrega em reais", example = "5.50")
    @NotNull(message = "Taxa de entrega é obrigatória")
    @DecimalMin(value = "0.0", message = "Taxa de entrega não pode ser negativa")
    private BigDecimal taxaEntrega;

    @Schema(description = "Tempo estimado de entrega em minutos", example = "45")
    @NotNull(message = "Tempo de entrega é obrigatório")
    @Min(value = 10, message = "Tempo mínimo é 10 minutos")
    @Max(value = 120, message = "Tempo máximo é 120 minutos")
    private Integer tempoEntrega;

    @Schema(description = "Horário de funcionamento", example = "18:00-23:00")
    @NotBlank(message = "Horário de funcionamento é obrigatório")
    private String horarioFuncionamento;
}
