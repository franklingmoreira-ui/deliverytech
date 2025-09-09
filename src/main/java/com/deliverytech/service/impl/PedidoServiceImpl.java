package com.deliverytech.service.impl;

import com.deliverytech.entity.Pedido;
import com.deliverytech.entity.StatusPedido;
import com.deliverytech.exception.EntityNotFoundException;
import com.deliverytech.repository.PedidoRepository;
import com.deliverytech.service.MetricsService;
import com.deliverytech.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MetricsService metricsService;

    @Override
    public Pedido criar(Pedido pedido) {
        pedido.setStatus(StatusPedido.CRIADO);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        metricsService.incrementarPedidosProcessados(pedido.getStatus().name());
        return pedidoSalvo;
    }

    @Override
    public Pedido buscarPorId(Long id) {
        long startTime = System.currentTimeMillis(); // MARCA O TEMPO INICIAL
        try {
            // Executa a busca no banco de dados
            return pedidoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com o ID: " + id));
        } finally {
            // O BLOCO FINALLY GARANTE QUE O TIMER SERÁ REGISTRADO MESMO SE OCORRER UM ERRO
            long duration = System.currentTimeMillis() - startTime; // Calcula a duração
            metricsService.registrarLatencia("buscar_pedido_id", duration); // Registra a métrica
        }
    }

    @Override
    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Pedido> listarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId);
    }

    @Override
    public Pedido atualizarStatus(Long id, StatusPedido status) {
        Pedido pedido = buscarPorId(id); // Este método já está sendo "timado"
        pedido.setStatus(status);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        metricsService.incrementarPedidosProcessados(status.name());
        return pedidoSalvo;
    }

    @Override
    public void cancelar(Long id) {
        Pedido pedido = buscarPorId(id); // Este método também será "timado"
        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
        metricsService.incrementarPedidosProcessados(StatusPedido.CANCELADO.name());
    }
}