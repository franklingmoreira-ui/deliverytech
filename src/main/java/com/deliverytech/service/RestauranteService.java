package com.deliverytech.service;

import com.deliverytech.entity.Restaurante;
import java.util.List;

public interface RestauranteService {
    Restaurante cadastrar(Restaurante restaurante);

    // CORREÇÃO AQUI: A interface agora "promete" retornar a entidade diretamente.
    Restaurante buscarPorId(Long id);

    List<Restaurante> listarTodos();
    List<Restaurante> buscarPorCategoria(String categoria);
    Restaurante atualizar(Long id, Restaurante restauranteAtualizado);
}
