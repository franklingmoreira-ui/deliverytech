package com.deliverytech.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal; // Use BigDecimal para dinheiro

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private String categoria; // <-- CAMPO ADICIONADO
    private BigDecimal preco; // <-- TIPO CORRIGIDO PARA BIGDECIMAL
    private boolean disponivel = true; // <-- CAMPO ADICIONADO

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    @JsonIgnore
    private Restaurante restaurante;
}
