# Adicionando Cache para Consultas Frequentes

## Dependências necessárias

Para adicionar cache ao projeto, é necessário incluir a seguinte dependência no `pom.xml`:

```xml
<!-- Spring Boot Cache -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

## Configuração do cache

### 1. Habilitar o cache na aplicação

Adicionar a anotação `@EnableCaching` na classe principal da aplicação:

```java
package com.deliverytech.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DeliveryApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApiApplication.class, args);
    }
}
```

### 2. Configurar o provedor de cache

Para desenvolvimento, pode-se usar o cache em memória do Spring. Para produção, recomenda-se usar um provedor mais robusto como Redis ou Ehcache.

Adicionar ao `application.properties`:

```properties
# Configuração do cache
spring.cache.type=simple
spring.cache.cache-names=clientes,pedidos,produtos,restaurantes,relatorios
```

## Implementação do cache nos repositórios

### ClienteRepository

```java
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Buscar cliente por email (método derivado)
    @Cacheable(cacheNames = "clientes", key = "#email")
    Optional<Cliente> findByEmail(String email);
    
    // Verificar se email já existe
    @Cacheable(cacheNames = "clientes", key = "#email + '_exists'")
    boolean existsByEmail(String email);
    
    // Buscar clientes ativos
    @Cacheable(cacheNames = "clientes", key = "'ativos'")
    List<Cliente> findByAtivoTrue();
    
    // Buscar clientes por nome (contendo)
    @Cacheable(cacheNames = "clientes", key = "#nome")
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    // Buscar clientes por telefone
    @Cacheable(cacheNames = "clientes", key = "#telefone")
    Optional<Cliente> findByTelefone(String telefone);
    
    // Query customizada - clientes com pedidos
    @Cacheable(cacheNames = "clientes", key = "'comPedidos'")
    @Query("SELECT DISTINCT c FROM Cliente c JOIN c.pedidos p WHERE c.ativo = true")
    List<Cliente> findClientesComPedidos();
    
    // Contar clientes ativos
    @Cacheable(cacheNames = "relatorios", key = "'countClientesAtivos'")
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.ativo = true")
    Long countClientesAtivos();
}
```

### PedidoRepository

```java
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Buscar pedidos por cliente
    @Cacheable(cacheNames = "pedidos", key = "#cliente.id")
    List<Pedido> findByClienteOrderByDataPedidoDesc(Cliente cliente);
    
    // Buscar pedidos por cliente ID
    @Cacheable(cacheNames = "pedidos", key = "#clienteId")
    List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);
    
    // Buscar por status
    @Cacheable(cacheNames = "pedidos", key = "#status")
    List<Pedido> findByStatusOrderByDataPedidoDesc(StatusPedido status);
    
    // Buscar por número do pedido
    @Cacheable(cacheNames = "pedidos", key = "#numeroPedido")
    Pedido findByNumeroPedido(String numeroPedido);
    
    // Buscar pedidos do dia
    @Cacheable(cacheNames = "pedidos", key = "'doDia'")
    @Query("SELECT p FROM Pedido p WHERE DATE(p.dataPedido) = CURRENT_DATE ORDER BY p.dataPedido DESC")
    List<Pedido> findPedidosDoDia();
    
    // Buscar pedidos por restaurante
    @Cacheable(cacheNames = "pedidos", key = "#restauranteId")
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId ORDER BY p.dataPedido DESC")
    List<Pedido> findByRestauranteId(@Param("restauranteId") Long restauranteId);
    
    // Relatório - pedidos por status
    @Cacheable(cacheNames = "relatorios", key = "'countPedidosByStatus'")
    @Query("SELECT p.status, COUNT(p) FROM Pedido p GROUP BY p.status")
    List<Object[]> countPedidosByStatus();
    
    // Pedidos pendentes (para dashboard)
    @Cacheable(cacheNames = "pedidos", key = "'pendentes'")
    @Query("SELECT p FROM Pedido p WHERE p.status IN ('PENDENTE', 'CONFIRMADO', 'PREPARANDO') ORDER BY p.dataPedido ASC")
    List<Pedido> findPedidosPendentes();
}
```

### ProdutoRepository

```java
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // Buscar produtos por restaurante
    @Cacheable(cacheNames = "produtos", key = "#restaurante.id")
    List<Produto> findByRestauranteAndDisponivelTrue(Restaurante restaurante);
    
    // Buscar produtos por restaurante ID
    @Cacheable(cacheNames = "produtos", key = "#restauranteId")
    List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);
    
    // Buscar por categoria
    @Cacheable(cacheNames = "produtos", key = "#categoria")
    List<Produto> findByCategoriaAndDisponivelTrue(String categoria);
    
    // Buscar por nome contendo
    @Cacheable(cacheNames = "produtos", key = "#nome")
    List<Produto> findByNomeContainingIgnoreCaseAndDisponivelTrue(String nome);
    
    // Ordenar por preço
    @Cacheable(cacheNames = "produtos", key = "'orderByPrecoAsc'")
    List<Produto> findByDisponivelTrueOrderByPrecoAsc();
    
    @Cacheable(cacheNames = "produtos", key = "'orderByPrecoDesc'")
    List<Produto> findByDisponivelTrueOrderByPrecoDesc();
    
    // Query customizada - produtos mais vendidos
    @Cacheable(cacheNames = "relatorios", key = "'produtosMaisVendidos'")
    @Query("SELECT p FROM Produto p JOIN p.itensPedido ip GROUP BY p ORDER BY COUNT(ip) DESC")
    List<Produto> findProdutosMaisVendidos();
}
```

### RestauranteRepository

```java
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    // Buscar por nome
    @Cacheable(cacheNames = "restaurantes", key = "#nome")
    Optional<Restaurante> findByNome(String nome);
    
    // Buscar restaurantes ativos
    @Cacheable(cacheNames = "restaurantes", key = "'ativos'")
    List<Restaurante> findByAtivoTrue();
    
    // Buscar por categoria
    @Cacheable(cacheNames = "restaurantes", key = "#categoria")
    List<Restaurante> findByCategoriaAndAtivoTrue(String categoria);
    
    // Buscar por nome contendo (case insensitive)
    @Cacheable(cacheNames = "restaurantes", key = "#nome + '_containing'")
    List<Restaurante> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    
    // Ordenar por avaliação (descendente)
    @Cacheable(cacheNames = "restaurantes", key = "'orderByAvaliacaoDesc'")
    List<Restaurante> findByAtivoTrueOrderByAvaliacaoDesc();
    
    // Query customizada - restaurantes com produtos
    @Cacheable(cacheNames = "restaurantes", key = "'comProdutos'")
    @Query("SELECT DISTINCT r FROM Restaurante r JOIN r.produtos p WHERE r.ativo = true")
    List<Restaurante> findRestaurantesComProdutos();
    
    // Categorias disponíveis
    @Cacheable(cacheNames = "relatorios", key = "'categoriasDisponiveis'")
    @Query("SELECT DISTINCT r.categoria FROM Restaurante r WHERE r.ativo = true ORDER BY r.categoria")
    List<String> findCategoriasDisponiveis();
}
```

## Invalidação de cache

Para manter a consistência dos dados, é necessário invalidar o cache quando os dados são modificados:

### ClienteRepository

```java
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // ... métodos com @Cacheable
    
    @CacheEvict(cacheNames = "clientes", key = "#result.email")
    @Override
    Cliente save(Cliente cliente);
    
    @CacheEvict(cacheNames = {"clientes", "relatorios"}, allEntries = true)
    @Override
    void deleteById(Long id);
}
```

### PedidoRepository

```java
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // ... métodos com @Cacheable
    
    @CacheEvict(cacheNames = "pedidos", key = "#result.numeroPedido")
    @Override
    Pedido save(Pedido pedido);
    
    @CacheEvict(cacheNames = {"pedidos", "relatorios"}, allEntries = true)
    @Override
    void deleteById(Long id);
}
```

### ProdutoRepository

```java
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // ... métodos com @Cacheable
    
    @CacheEvict(cacheNames = "produtos", key = "#result.id")
    @Override
    Produto save(Produto produto);
    
    @CacheEvict(cacheNames = {"produtos", "relatorios"}, allEntries = true)
    @Override
    void deleteById(Long id);
}
```

### RestauranteRepository

```java
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    // ... métodos com @Cacheable
    
    @CacheEvict(cacheNames = "restaurantes", key = "#result.id")
    @Override
    Restaurante save(Restaurante restaurante);
    
    @CacheEvict(cacheNames = {"restaurantes", "relatorios"}, allEntries = true)
    @Override
    void deleteById(Long id);
}
```

## Benefícios do cache

1. **Melhor performance**: Reduz o tempo de resposta das consultas frequentes
2. **Menor carga no banco de dados**: Diminui a quantidade de consultas ao banco de dados
3. **Melhor experiência do usuário**: Aumenta a responsividade da API

## Considerações importantes

1. **Consistência de dados**: É importante invalidar o cache quando os dados são modificados para manter a consistência
2. **Tamanho do cache**: Configurar o tamanho máximo do cache para evitar consumo excessivo de memória
3. **Tempo de expiração**: Configurar o tempo de expiração do cache para garantir que os dados não fiquem desatualizados por muito tempo
4. **Monitoramento**: Monitorar o uso do cache para identificar oportunidades de otimização