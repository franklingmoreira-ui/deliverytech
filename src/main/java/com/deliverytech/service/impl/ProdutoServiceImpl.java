package com.deliverytech.service.impl;

import com.deliverytech.entity.Produto;
import com.deliverytech.exception.EntityNotFoundException;
import com.deliverytech.repository.ProdutoRepository;
import com.deliverytech.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Override
    @CacheEvict(value = "produtos", allEntries = true) // ✅ LIMPA todo o cache de produtos ao adicionar um novo
    public Produto cadastrar(Produto produto) {
        System.out.println("### CADASTRANDO PRODUTO NO BANCO E LIMPANDO CACHE...");
        return produtoRepository.save(produto);
    }

    @Override
    @Cacheable(value = "produtos", key = "#id") // ✅ ARMAZENA o resultado deste método no cache
    public Produto buscarPorId(Long id) {
        System.out.println("### BUSCANDO PRODUTO DO BANCO DE DADOS (CACHE MISS): ID " + id);
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com o ID: " + id));
    }

    @Override
    public List<Produto> buscarPorRestaurante(Long restauranteId) {
        // Nota: Poderíamos cachear esta lista também com @Cacheable(value = "produtosPorRestaurante", key="#restauranteId")
        return produtoRepository.findByRestauranteId(restauranteId);
    }

    @Override
    @CacheEvict(value = "produtos", key = "#id") // ✅ REMOVE a entrada específica do cache ao atualizar
    public Produto atualizar(Long id, Produto atualizado) {
        System.out.println("### ATUALIZANDO PRODUTO NO BANCO E LIMPANDO CACHE: ID " + id);
        Produto existente = buscarPorId(id);

        existente.setNome(atualizado.getNome());
        existente.setDescricao(atualizado.getDescricao());
        existente.setCategoria(atualizado.getCategoria());
        existente.setPreco(atualizado.getPreco());

        return produtoRepository.save(existente);
    }

    @Override
    @CacheEvict(value = "produtos", key = "#id") // ✅ REMOVE a entrada específica do cache ao alterar disponibilidade
    public void alterarDisponibilidade(Long id, boolean disponivel) {
        System.out.println("### ALTERANDO DISPONIBILIDADE NO BANCO E LIMPANDO CACHE: ID " + id);
        Produto produto = buscarPorId(id);
        produto.setDisponivel(disponivel);
        produtoRepository.save(produto);
    }
}
