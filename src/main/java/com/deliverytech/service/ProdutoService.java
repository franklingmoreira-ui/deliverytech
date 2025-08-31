package com.deliverytech.service;

import com.deliverytech.entity.Produto;
import java.util.List;

public interface ProdutoService {
    Produto cadastrar(Produto produto);
    // Retorna a entidade diretamente ou lança uma exceção
    Produto buscarPorId(Long id);
    List<Produto> buscarPorRestaurante(Long restauranteId);
    Produto atualizar(Long id, Produto produtoAtualizado);
    void alterarDisponibilidade(Long id, boolean disponivel);
}