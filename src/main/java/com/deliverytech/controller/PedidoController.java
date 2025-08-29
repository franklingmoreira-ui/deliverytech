package com.deliverytech.controller;

import com.deliverytech.dto.request.PedidoRequest;
import com.deliverytech.dto.response.ItemPedidoResponse;
import com.deliverytech.dto.response.PedidoResponse;
import com.deliverytech.entity.*;
import com.deliverytech.service.ClienteService;
import com.deliverytech.service.PedidoService;
import com.deliverytech.service.ProdutoService;
import com.deliverytech.service.RestauranteService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

private final PedidoService pedidoService;
private final ClienteService clienteService;
private final RestauranteService restauranteService;
private final ProdutoService produtoService;

@PostMapping
public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Restaurante restaurante = restauranteService.buscarPorId(request.getRestauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        List<ItemPedido> itens = request.getItens().stream().map(item -> {
        Produto produto = produtoService.buscarPorId(item.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        ItemPedido itemPedido = ItemPedido.builder()
                .produto(produto)
                .quantidade(item.getQuantidade())
                .precoUnitario(produto.getPreco())
                .build();
        itemPedido.calcularSubtotal();
        return itemPedido;
        }).collect(Collectors.toList());

        BigDecimal total = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .restaurante(restaurante)
                .status(StatusPedido.CRIADO)
                .total(total)
                .enderecoEntrega(request.getEnderecoEntrega())
                .itens(itens)
                .build();
        
        // Associa cada item ao pedido principal
        pedido.getItens().forEach(item -> item.setPedido(pedido));

        Pedido salvo = pedidoService.criar(pedido);
        List<ItemPedidoResponse> itensResp = salvo.getItens().stream()
                .map(i -> new ItemPedidoResponse(i.getProduto().getId(), i.getProduto().getNome(), i.getQuantidade(), i.getPrecoUnitario()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PedidoResponse(
                salvo.getId(),
                cliente.getId(),
                restaurante.getId(),
                salvo.getEnderecoEntrega(),
                salvo.getTotal(),
                salvo.getStatus(),
                salvo.getDataPedido(),
                itensResp
        ));
}

    // Você pode adicionar os outros endpoints (buscar, listar, etc.) aqui depois.
}
