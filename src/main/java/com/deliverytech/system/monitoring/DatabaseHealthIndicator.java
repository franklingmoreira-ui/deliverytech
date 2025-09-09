package com.deliverytech.system.monitoring; // ✅ CORRIGIDO

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component("database") // Define o nome deste health check como "database"
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            // Usamos uma query simples e rápida que funciona na maioria dos bancos
            statement.execute("SELECT 1");
            return Health.up().withDetail("message", "Conexão com o banco de dados bem-sucedida").build();
        } catch (Exception e) {
            // Se qualquer exceção ocorrer, consideramos o serviço "DOWN"
            return Health.down()
                        .withDetail("error", e.getMessage())
                        .build();
        }
    }
}
