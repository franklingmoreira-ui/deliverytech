package com.deliverytech.controller;

import com.deliverytech.dto.request.RestauranteRequest;
import com.deliverytech.dto.response.RestauranteResponse;
import com.deliverytech.entity.Restaurante;
import com.deliverytech.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final RestauranteService restauranteService;

    @PostMapping
    public ResponseEntity<RestauranteResponse> cadastrar(@Valid @RequestBody RestauranteRequest request) {
        Restaurante restaurante = Restaurante.builder()
                .nome(request.getNome())
                .telefone(request.getTelefone())
                .categoria(request.getCategoria())
                .endereco(request.getEndereco())
                .horarioFuncionamento(request.getHorarioFuncionamento())
                .taxaEntrega(request.getTaxaEntrega())
                .tempoEntrega(request.getTempoEntrega())
                .ativo(true)
                .build();
        Restaurante salvo = restauranteService.cadastrar(restaurante);
        return ResponseEntity.ok(new RestauranteResponse(
                salvo.getId(), salvo.getNome(), salvo.getCategoria(), salvo.getEndereco(), salvo.getTelefone(),
                salvo.getTaxaEntrega(), salvo.getTempoEntrega(), salvo.getHorarioFuncionamento(), salvo.isAtivo()));
    }

    @GetMapping
    public List<RestauranteResponse> listarTodos() {
        return restauranteService.listarTodos().stream()
                .map(r -> new RestauranteResponse(r.getId(), r.getNome(), r.getCategoria(), r.getEndereco(), r.getTelefone(), r.getTaxaEntrega(), r.getTempoEntrega(), r.getHorarioFuncionamento(), r.isAtivo()))
                .collect(Collectors.toList());
    }
    
    // Adicione os outros métodos (buscarPorId, atualizar, etc.) seguindo exatamente o mesmo padrão.
}
