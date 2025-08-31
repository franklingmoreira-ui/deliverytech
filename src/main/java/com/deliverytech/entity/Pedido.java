package com.deliverytech.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pedidos") // É uma boa prática definir o nome da tabela
public class Pedido {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false) // Garante que todo pedido tenha um cliente
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false) // Garante que todo pedido tenha um restaurante
    private Restaurante restaurante;

    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.CRIADO;

    @Builder.Default
    private LocalDateTime dataPedido = LocalDateTime.now();

    // Inicializando a lista para evitar NullPointerException
    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @Embedded
    private Endereco enderecoEntrega;
}
