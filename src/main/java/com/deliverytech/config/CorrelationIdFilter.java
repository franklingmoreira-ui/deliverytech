package com.deliverytech.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements Filter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. Tenta pegar o Correlation ID do header da requisição
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        // 2. Se não existir, gera um novo
        if (!StringUtils.hasText(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        try {
            // 3. Adiciona o ID ao MDC (Mapped Diagnostic Context) do SLF4J
            // O logback-spring.xml já está configurado para incluir o MDC nos logs
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

            // 4. Adiciona o ID ao header da resposta para o cliente
            response.addHeader(CORRELATION_ID_HEADER, correlationId);

            // 5. Continua o processamento da requisição
            filterChain.doFilter(request, response);

        } finally {
            // 6. Limpa o MDC para evitar que o ID vaze para outras requisições
            MDC.remove(CORRELATION_ID_MDC_KEY);
        }
    }
}