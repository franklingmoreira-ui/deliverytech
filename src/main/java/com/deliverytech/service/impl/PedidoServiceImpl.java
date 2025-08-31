package com.deliverytech.service.impl;

import com.deliverytech.entity.Pedido;
import com.deliverytech.entity.StatusPedido;
import com.deliverytech.exception.EntityNotFoundException; // Importe a nova exceção
import com.deliverytech.repository.PedidoRepository;
import com.deliverytech.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    @Override
    public Pedido criar(Pedido pedido) {
        pedido.setStatus(StatusPedido.CRIADO);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido buscarPorId(Long id) {
        // ATUALIZAÇÃO AQUI: Retorna a entidade diretamente ou lança a exceção
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com o ID: " + id));
    }

    @Override
    public List<Pedido> listarPorCliente(Long clienteId) {
        // Correção aqui: removido o "Long" da chamada do método
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Pedido> listarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId);
    }

    @Override
    public Pedido atualizarStatus(Long id, StatusPedido status) {
        // Reutiliza o método buscarPorId que já trata o erro de "não encontrado"
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }

    @Override
    public void cancelar(Long id) {
        // Reutiliza o método buscarPorId para garantir que o pedido existe antes de cancelar
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }
}
