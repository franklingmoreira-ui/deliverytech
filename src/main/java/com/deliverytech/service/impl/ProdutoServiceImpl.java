package com.deliverytech.service.impl;

import com.deliverytech.entity.Produto;
import com.deliverytech.exception.EntityNotFoundException;
import com.deliverytech.repository.ProdutoRepository;
import com.deliverytech.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Override
    public Produto cadastrar(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Override
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com o ID: " + id));
    }

    @Override
    public List<Produto> buscarPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId);
    }

    @Override
    public Produto atualizar(Long id, Produto atualizado) {
        // Reutiliza o método buscarPorId que já trata o erro de "não encontrado"
        Produto existente = buscarPorId(id);
        
        // Atualiza os campos do produto existente com os novos dados
        existente.setNome(atualizado.getNome());
        existente.setDescricao(atualizado.getDescricao());
        existente.setCategoria(atualizado.getCategoria());
        existente.setPreco(atualizado.getPreco());
        
        return produtoRepository.save(existente);
    }

    @Override
    public void alterarDisponibilidade(Long id, boolean disponivel) {
        // Reutiliza o método buscarPorId para garantir que o produto existe antes de alterar
        Produto produto = buscarPorId(id);
        produto.setDisponivel(disponivel);
        produtoRepository.save(produto);
    }
}
