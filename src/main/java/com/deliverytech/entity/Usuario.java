package com.deliverytech.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuarios") // Nome da tabela no plural é uma boa prática
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // Email deve ser único e obrigatório
    private String email;

    @Column(nullable = false) // Senha é obrigatória
    private String senha;

    @Column(nullable = false) // Nome é obrigatório
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // Papel é obrigatório
    private Role role;

    @Builder.Default // Corrige o aviso do Lombok
    private Boolean ativo = true;

    @Builder.Default // Corrige o aviso do Lombok
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // Sugestão: Usar o relacionamento direto em vez de apenas o ID
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id") // Opcional, apenas para usuários do tipo RESTAURANTE
    private Restaurante restaurante;
}
