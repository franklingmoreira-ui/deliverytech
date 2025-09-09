package com.deliverytech.system.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;

@Component("paymentGateway") // Define o nome do health check
public class PaymentGatewayHealthIndicator implements HealthIndicator {

    private static final String SERVICE_URL = "https://api.gatewaypagamento.com/status";

    @Override
    public Health health() {
        if (isServiceUp()) {
            // Se o serviço estiver online, retorna UP com detalhes
            return Health.up()
                        .withDetail("url", SERVICE_URL)
                        .withDetail("status", "Serviço está operacional.")
                        .build();
        } else {
            // Se estiver offline, retorna DOWN com detalhes do erro
            return Health.down()
                        .withDetail("url", SERVICE_URL)
                        .withDetail("error", "Serviço indisponível ou respondendo com erro.")
                        .build();
        }
    }

    /**
     * Método privado que simula a chamada ao serviço externo.
     * Para fins de demonstração, ele retorna 'true' (UP) em 80% das vezes
     * e 'false' (DOWN) em 20% das vezes.
     */
    private boolean isServiceUp() {
        // Gera um número aleatório entre 0 e 10
        int random = ThreadLocalRandom.current().nextInt(10);
        // Se o número for menor que 8, consideramos o serviço UP (80% de chance)
        return random < 8;
    }
}
