package com.deliverytech.service.impl;

import com.deliverytech.entity.Cliente;
import com.deliverytech.exception.EntityNotFoundException;
import com.deliverytech.repository.ClienteRepository;
import com.deliverytech.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public Cliente cadastrar(Cliente cliente) {
        // Lógica de negócio, como verificar se o e-mail já existe, poderia ser adicionada aqui
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o ID: " + id));
    }

    @Override
    public List<Cliente> listarAtivos() {
        return clienteRepository.findByAtivoTrue();
    }

    @Override
    public Cliente atualizar(Long id, Cliente atualizado) {
        // Reutiliza o buscarPorId que já trata o erro de "não encontrado"
        Cliente existente = buscarPorId(id);

        // Atualiza os campos do cliente existente com os novos dados
        existente.setNome(atualizado.getNome());
        existente.setEmail(atualizado.getEmail());
        existente.setTelefone(atualizado.getTelefone());
        existente.setEndereco(atualizado.getEndereco());
        
        return clienteRepository.save(existente);
    }

    @Override
    public void ativarDesativar(Long id) {
        Cliente cliente = buscarPorId(id);
        // Correção aqui: usa isAtivo() para boolean
        cliente.setAtivo(!cliente.isAtivo());
        clienteRepository.save(cliente);
    }
}
