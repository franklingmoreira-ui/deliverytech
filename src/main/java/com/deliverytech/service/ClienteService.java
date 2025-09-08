package com.deliverytech.service;

import com.deliverytech.entity.Cliente;
import java.util.List;

public interface ClienteService {
    Cliente cadastrar(Cliente cliente);
    // CORREÇÃO AQUI: Retorna a entidade diretamente
    Cliente buscarPorId(Long id);
    List<Cliente> listarAtivos();
    Cliente atualizar(Long id, Cliente clienteAtualizado);
    void ativarDesativar(Long id);
}
