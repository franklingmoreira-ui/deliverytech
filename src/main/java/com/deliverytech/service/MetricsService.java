package com.deliverytech.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer; // ✅ 1. IMPORTAR O TIMER
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit; // ✅ 2. IMPORTAR TIMEUNIT

@Service
public class MetricsService {

    private final MeterRegistry meterRegistry;

    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void incrementarPedidosProcessados(String status) {
        Counter.builder("pedidos_processados_total")
            .description("Total de pedidos processados pelo sistema")
            .tag("status", status)
            .register(meterRegistry)
            .increment();
    }

    /**
     * ✅ 3. NOVO MÉTODO PARA REGISTRAR A LATÊNCIA
     * Registra o tempo de execução de uma operação.
     *
     * @param nomeMetrica O nome da métrica do timer (ex: "buscar_pedido_id")
     * @param tempoMs     O tempo de execução em milissegundos.
     */
    public void registrarLatencia(String nomeMetrica, long tempoMs) {
        Timer.builder(nomeMetrica + "_latency_seconds")
            .description("Mede a latência de operações críticas")
            .register(meterRegistry)
             .record(tempoMs, TimeUnit.MILLISECONDS); // Registra o tempo
    }
}