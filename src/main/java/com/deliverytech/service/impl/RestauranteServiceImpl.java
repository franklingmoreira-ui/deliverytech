package com.deliverytech.service.impl;

import com.deliverytech.entity.Restaurante;
import com.deliverytech.exception.EntityNotFoundException; // Importe a nova exceção
import com.deliverytech.repository.RestauranteRepository;
import com.deliverytech.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository restauranteRepository;

    @Override
    public Restaurante cadastrar(Restaurante restaurante) {
        // Você poderia adicionar lógicas de negócio aqui, como verificar se o nome já existe
        return restauranteRepository.save(restaurante);
    }

    @Override
    public Restaurante buscarPorId(Long id) {
        // ATUALIZAÇÃO AQUI: Lança a exceção específica e retorna a entidade diretamente
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com o ID: " + id));
    }

    @Override
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }

    @Override
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }

    @Override
    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Restaurante restauranteExistente = buscarPorId(id); // Reutiliza o método que já lança a exceção

        // Atualiza os campos do restaurante existente com os novos dados
        restauranteExistente.setNome(restauranteAtualizado.getNome());
        restauranteExistente.setTelefone(restauranteAtualizado.getTelefone());
        restauranteExistente.setCategoria(restauranteAtualizado.getCategoria());
        restauranteExistente.setTaxaEntrega(restauranteAtualizado.getTaxaEntrega());
        restauranteExistente.setTempoEntrega(restauranteAtualizado.getTempoEntrega());
        restauranteExistente.setEndereco(restauranteAtualizado.getEndereco());
        restauranteExistente.setHorarioFuncionamento(restauranteAtualizado.getHorarioFuncionamento());
        
        return restauranteRepository.save(restauranteExistente);
    }
}
