# Melhorias no Tratamento de Transações

## Análise atual do tratamento de transações

Os serviços atualmente estão utilizando a anotação `@Transactional` no nível da classe, o que significa que todos os métodos públicos do serviço são transacionais por padrão. Além disso, alguns métodos que apenas realizam consultas estão utilizando `@Transactional(readOnly = true)` para indicar que são transações de leitura.

## Sugestões de melhorias

### 1. Configuração de transações específicas por método

Em vez de aplicar `@Transactional` a toda a classe, é recomendável aplicar a anotação individualmente a cada método, permitindo configurar propriedades específicas para cada operação.

#### Exemplo para ClienteService:

```java
@Service
public class ClienteService {
    
    // Remover @Transactional da classe
    
    /**
     * Cadastrar novo cliente
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, 
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public Cliente cadastrar(Cliente cliente) {
        // ... implementação
    }
    
    /**
     * Buscar cliente por ID
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    /**
     * Atualizar dados do cliente
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        // ... implementação
    }
    
    /**
     * Inativar cliente (soft delete)
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public void inativar(Long id) {
        // ... implementação
    }
}
```

### 2. Configuração de transações para PedidoService

O serviço `PedidoService` tem operações mais complexas que poderiam se beneficiar de configurações de transação mais específicas:

#### Exemplo para PedidoService:

```java
@Service
public class PedidoService {
    
    // Remover @Transactional da classe
    
    /**
     * Criar novo pedido
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public Pedido criarPedido(Long clienteId, Long restauranteId) {
        // ... implementação
    }
    
    /**
     * Adicionar item ao pedido
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, // Para evitar condições de corrida
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade) {
        // ... implementação
    }
    
    /**
     * Confirmar pedido
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, // Para garantir consistência durante a confirmação
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public Pedido confirmarPedido(Long pedidoId) {
        // ... implementação
    }
    
    /**
     * Cancelar pedido
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public Pedido cancelarPedido(Long pedidoId, String motivo) {
        // ... implementação
    }
}
```

### 3. Configuração de transações para ProdutoService

#### Exemplo para ProdutoService:

```java
@Service
public class ProdutoService {
    
    // Remover @Transactional da classe
    
    /**
     * Cadastrar novo produto
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public Produto cadastrar(Produto produto, Long restauranteId) {
        // ... implementação
    }
    
    /**
     * Atualizar produto
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        // ... implementação
    }
    
    /**
     * Alterar disponibilidade
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public void alterarDisponibilidade(Long id, boolean disponivel) {
        // ... implementação
    }
}
```

### 4. Configuração de transações para RestauranteService

#### Exemplo para RestauranteService:

```java
@Service
public class RestauranteService {
    
    // Remover @Transactional da classe
    
    /**
     * Cadastrar novo restaurante
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public Restaurante cadastrar(Restaurante restaurante) {
        // ... implementação
    }
    
    /**
     * Atualizar restaurante
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        // ... implementação
    }
    
    /**
     * Inativar restaurante
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
                   propagation = Propagation.REQUIRED,
                   timeout = 30)
    public void inativar(Long id) {
        // ... implementação
    }
}
```

## Benefícios das melhorias

1. **Controle mais preciso**: Configurar transações específicas por método permite um controle mais preciso sobre o comportamento transacional de cada operação.

2. **Melhor performance**: Evitar transações desnecessárias em métodos que apenas realizam consultas pode melhorar a performance do sistema.

3. **Consistência de dados**: Utilizar níveis de isolamento apropriados pode garantir a consistência dos dados durante operações concorrentes.

4. **Tratamento de erros**: Configurar timeouts pode ajudar a identificar e tratar problemas de performance ou deadlock mais rapidamente.

## Considerações adicionais

1. **Isolation Levels**: Os níveis de isolamento devem ser escolhidos com base nas necessidades específicas de cada método:
   - `READ_UNCOMMITTED`: Menor isolamento, permite leitura de dados não confirmados
   - `READ_COMMITTED`: Padrão, evita leitura de dados não confirmados
   - `REPEATABLE_READ`: Evita condições de corrida em leituras repetidas
   - `SERIALIZABLE`: Maior isolamento, serializa todas as transações

2. **Propagation Types**: Os tipos de propagação devem ser escolhidos com base na necessidade de criar ou participar de transações existentes:
   - `REQUIRED`: Cria uma nova transação se não existir, ou participa da transação existente
   - `REQUIRES_NEW`: Sempre cria uma nova transação, suspendendo a existente se necessário
   - `SUPPORTS`: Executa dentro de uma transação se existir, ou sem transação se não existir
   - `NOT_SUPPORTED`: Executa sem transação, suspendendo a existente se necessário
   - `MANDATORY`: Requer uma transação existente
   - `NEVER`: Não permite execução dentro de uma transação
   - `NESTED`: Executa dentro de uma transação aninhada se existir

3. **Timeouts**: Configurar timeouts apropriados pode ajudar a evitar que transações fiquem presas por muito tempo, melhorando a responsividade do sistema.