# Implementação de Auditoria (Logs) para Operações Importantes

## Dependências necessárias

Para implementar auditoria com logs, é necessário incluir a seguinte dependência no `pom.xml`:

```xml
<!-- Spring Boot AOP -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

## Configuração do logging

### 1. Configurar o logger

Adicionar ao `application.properties`:

```properties
# Configuração do logging
logging.level.com.deliverytech.delivery_api.audit=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
logging.file.name=logs/audit.log
```

### 2. Criar anotação personalizada para auditoria

```java
package com.deliverytech.delivery_api.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditavel {
    String value() default "";
}
```

### 3. Criar aspecto para auditoria

```java
package com.deliverytech.delivery_api.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    @Around("@annotation(auditavel)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditavel auditavel) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        // Log de início da operação
        logger.info("INICIO_AUDIT - {} - {} - {} - Parâmetros: {}", 
                   LocalDateTime.now(), className, methodName, args);

        try {
            // Executar o método
            Object result = joinPoint.proceed();

            // Log de sucesso
            long endTime = System.currentTimeMillis();
            logger.info("SUCESSO_AUDIT - {} - {} - {} - Tempo: {}ms - Resultado: {}", 
                       LocalDateTime.now(), className, methodName, (endTime - startTime), result);

            return result;
        } catch (Exception e) {
            // Log de erro
            long endTime = System.currentTimeMillis();
            logger.error("ERRO_AUDIT - {} - {} - {} - Tempo: {}ms - Erro: {}", 
                        LocalDateTime.now(), className, methodName, (endTime - startTime), e.getMessage(), e);

            throw e;
        }
    }
}
```

## Implementação da auditoria nos serviços

### ClienteService

```java
@Service
public class ClienteService {
    
    // ... outros imports
    
    @Autowired
    private AuditLogger auditLogger;
    
    /**
     * Cadastrar novo cliente
     */
    @Auditavel("Cadastro de cliente")
    public Cliente cadastrar(Cliente cliente) {
        // ... implementação
    }
    
    /**
     * Atualizar dados do cliente
     */
    @Auditavel("Atualização de cliente")
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        // ... implementação
    }
    
    /**
     * Inativar cliente (soft delete)
     */
    @Auditavel("Inativação de cliente")
    public void inativar(Long id) {
        // ... implementação
    }
}
```

### PedidoService

```java
@Service
public class PedidoService {
    
    // ... outros imports
    
    @Autowired
    private AuditLogger auditLogger;
    
    /**
     * Criar novo pedido
     */
    @Auditavel("Criação de pedido")
    public Pedido criarPedido(Long clienteId, Long restauranteId) {
        // ... implementação
    }
    
    /**
     * Adicionar item ao pedido
     */
    @Auditavel("Adição de item ao pedido")
    public Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade) {
        // ... implementação
    }
    
    /**
     * Confirmar pedido
     */
    @Auditavel("Confirmação de pedido")
    public Pedido confirmarPedido(Long pedidoId) {
        // ... implementação
    }
    
    /**
     * Cancelar pedido
     */
    @Auditavel("Cancelamento de pedido")
    public Pedido cancelarPedido(Long pedidoId, String motivo) {
        // ... implementação
    }
}
```

### ProdutoService

```java
@Service
public class ProdutoService {
    
    // ... outros imports
    
    @Autowired
    private AuditLogger auditLogger;
    
    /**
     * Cadastrar novo produto
     */
    @Auditavel("Cadastro de produto")
    public Produto cadastrar(Produto produto, Long restauranteId) {
        // ... implementação
    }
    
    /**
     * Atualizar produto
     */
    @Auditavel("Atualização de produto")
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        // ... implementação
    }
    
    /**
     * Alterar disponibilidade
     */
    @Auditavel("Alteração de disponibilidade de produto")
    public void alterarDisponibilidade(Long id, boolean disponivel) {
        // ... implementação
    }
}
```

### RestauranteService

```java
@Service
public class RestauranteService {
    
    // ... outros imports
    
    @Autowired
    private AuditLogger auditLogger;
    
    /**
     * Cadastrar novo restaurante
     */
    @Auditavel("Cadastro de restaurante")
    public Restaurante cadastrar(Restaurante restaurante) {
        // ... implementação
    }
    
    /**
     * Atualizar restaurante
     */
    @Auditavel("Atualização de restaurante")
    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        // ... implementação
    }
    
    /**
     * Inativar restaurante
     */
    @Auditavel("Inativação de restaurante")
    public void inativar(Long id) {
        // ... implementação
    }
}
```

## Criação de um logger de auditoria personalizado

```java
package com.deliverytech.delivery_api.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {
    
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    
    public void log(String operacao, String detalhes) {
        auditLogger.info("{} - {} - {}", 
                        java.time.LocalDateTime.now(), 
                        operacao, 
                        detalhes);
    }
    
    public void logSucesso(String operacao, String detalhes) {
        auditLogger.info("SUCESSO - {} - {} - {}", 
                        java.time.LocalDateTime.now(), 
                        operacao, 
                        detalhes);
    }
    
    public void logErro(String operacao, String detalhes, Exception e) {
        auditLogger.error("ERRO - {} - {} - {} - {}", 
                         java.time.LocalDateTime.now(), 
                         operacao, 
                         detalhes, 
                         e.getMessage(), 
                         e);
    }
}
```

## Exemplo de uso do logger de auditoria personalizado

```java
@Service
public class ClienteService {
    
    @Autowired
    private AuditLogger auditLogger;
    
    public Cliente cadastrar(Cliente cliente) {
        try {
            // Validar email único
            if (clienteRepository.existsByEmail(cliente.getEmail())) {
                auditLogger.logErro("Cadastro de cliente", "Email já cadastrado: " + cliente.getEmail(), 
                                   new IllegalArgumentException("Email já cadastrado"));
                throw new IllegalArgumentException("Email já cadastrado: " + cliente.getEmail());
            }
            
            // Validações de negócio
            validarDadosCliente(cliente);
            
            // Definir como ativo por padrão
            cliente.setAtivo(true);
            
            Cliente clienteSalvo = clienteRepository.save(cliente);
            auditLogger.logSucesso("Cadastro de cliente", "Cliente cadastrado com sucesso: " + clienteSalvo.getId());
            
            return clienteSalvo;
        } catch (Exception e) {
            auditLogger.logErro("Cadastro de cliente", "Erro ao cadastrar cliente", e);
            throw e;
        }
    }
}
```

## Benefícios da auditoria

1. **Rastreabilidade**: Permite rastrear todas as operações importantes realizadas no sistema
2. **Segurança**: Ajuda a identificar acessos não autorizados ou tentativas de violação de segurança
3. **Conformidade**: Facilita a conformidade com regulamentações que exigem auditoria de operações
4. **Debugging**: Facilita a identificação e resolução de problemas através do histórico de operações
5. **Análise de desempenho**: Permite analisar o tempo de execução das operações para identificar gargalos

## Considerações importantes

1. **Nível de detalhe**: É importante definir o nível adequado de detalhe nos logs para não impactar a performance do sistema
2. **Armazenamento**: Configurar um sistema de armazenamento adequado para os logs de auditoria, com rotação de arquivos para evitar consumo excessivo de disco
3. **Segurança**: Proteger os logs de auditoria com permissões adequadas para evitar acesso não autorizado
4. **Monitoramento**: Configurar alertas para operações suspeitas ou erros frequentes
5. **Retenção**: Definir uma política de retenção de logs para evitar armazenamento indefinido de dados