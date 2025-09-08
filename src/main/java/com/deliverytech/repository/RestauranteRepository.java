package com.deliverytech.repository;

import com.deliverytech.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar por nome (para validações, por exemplo)
    Optional<Restaurante> findByNome(String nome);

    // Buscar por categoria (corrigido para bater com o que o Service precisa)
    List<Restaurante> findByCategoria(String categoria);

    // Você pode adicionar os outros métodos de volta quando precisar deles.
    // Por enquanto, vamos manter simples para fazer o projeto compilar.
}
