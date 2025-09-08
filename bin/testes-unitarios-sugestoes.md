# Sugestões de Testes Unitários para os Serviços

## ClienteService

### Métodos a serem testados:

1. **cadastrar(Cliente cliente)**
   - Testar cadastro bem-sucedido de um novo cliente
   - Testar tentativa de cadastro com email já existente (deve lançar IllegalArgumentException)
   - Testar tentativa de cadastro com dados inválidos (nome vazio, email vazio, nome com menos de 2 caracteres)

2. **buscarPorId(Long id)**
   - Testar busca por ID existente (deve retornar o cliente)
   - Testar busca por ID inexistente (deve retornar Optional.empty())

3. **buscarPorEmail(String email)**
   - Testar busca por email existente (deve retornar o cliente)
   - Testar busca por email inexistente (deve retornar Optional.empty())

4. **listarAtivos()**
   - Testar listagem de clientes ativos (deve retornar apenas clientes com ativo=true)

5. **atualizar(Long id, Cliente clienteAtualizado)**
   - Testar atualização bem-sucedida de um cliente existente
   - Testar tentativa de atualização de cliente inexistente (deve lançar IllegalArgumentException)
   - Testar tentativa de atualização com email já existente (deve lançar IllegalArgumentException)

6. **inativar(Long id)**
   - Testar inativação bem-sucedida de um cliente existente
   - Testar tentativa de inativação de cliente inexistente (deve lançar IllegalArgumentException)

7. **buscarPorNome(String nome)**
   - Testar busca por nome existente (deve retornar clientes cujo nome contém o texto informado)

### Exemplo de estrutura de teste:

```java
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void cadastrar_ClienteValido_DeveRetornarClienteSalvo() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao@example.com");
        cliente.setTelefone("123456789");
        cliente.setEndereco("Rua A, 123");

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.cadastrar(cliente);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.isAtivo()).isTrue();
        verify(clienteRepository).save(cliente);
    }

    @Test
    void cadastrar_EmailJaExistente_DeveLancarIllegalArgumentException() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao@example.com");
        cliente.setTelefone("123456789");
        cliente.setEndereco("Rua A, 123");

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> clienteService.cadastrar(cliente))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email já cadastrado: " + cliente.getEmail());
    }
}
```

## PedidoService

### Métodos a serem testados:

1. **criarPedido(Long clienteId, Long restauranteId)**
   - Testar criação bem-sucedida de um novo pedido
   - Testar tentativa de criação com cliente inexistente (deve lançar IllegalArgumentException)
   - Testar tentativa de criação com restaurante inexistente (deve lançar IllegalArgumentException)
   - Testar tentativa de criação com cliente inativo (deve lançar IllegalArgumentException)
   - Testar tentativa de criação com restaurante inativo (deve lançar IllegalArgumentException)

2. **adicionarItem(Long pedidoId, Long produtoId, Integer quantidade)**
   - Testar adição bem-sucedida de item ao pedido
   - Testar tentativa de adição a pedido inexistente (deve lançar IllegalArgumentException)
   - Testar tentativa de adição de produto inexistente (deve lançar IllegalArgumentException)
   - Testar tentativa de adição de produto indisponível (deve lançar IllegalArgumentException)
   - Testar tentativa de adição com quantidade inválida (menor ou igual a zero)
   - Testar tentativa de adição de produto de outro restaurante (deve lançar IllegalArgumentException)

3. **confirmarPedido(Long pedidoId)**
   - Testar confirmação bem-sucedida de pedido pendente
   - Testar tentativa de confirmação de pedido inexistente (deve lançar IllegalArgumentException)
   - Testar tentativa de confirmação de pedido com status diferente de PENDENTE (deve lançar IllegalArgumentException)
   - Testar tentativa de confirmação de pedido sem itens (deve lançar IllegalArgumentException)

4. **buscarPorId(Long id)**
   - Testar busca por ID existente (deve retornar o pedido)
   - Testar busca por ID inexistente (deve retornar Optional.empty())

5. **listarPorCliente(Long clienteId)**
   - Testar listagem de pedidos por cliente

6. **cancelarPedido(Long pedidoId, String motivo)**
   - Testar cancelamento bem-sucedido de pedido
   - Testar tentativa de cancelamento de pedido inexistente (deve lançar IllegalArgumentException)
   - Testar tentativa de cancelamento de pedido já entregue (deve lançar IllegalArgumentException)
   - Testar tentativa de cancelamento de pedido já cancelado (deve lançar IllegalArgumentException)

## ProdutoService

### Métodos a serem testados:

1. **cadastrar(Produto produto, Long restauranteId)**
   - Testar cadastro bem-sucedido de um novo produto
   - Testar tentativa de cadastro com restaurante inexistente (deve lançar IllegalArgumentException)
   - Testar tentativa de cadastro com dados inválidos (nome vazio, preço inválido)

2. **buscarPorId(Long id)**
   - Testar busca por ID existente (deve retornar o produto)
   - Testar busca por ID inexistente (deve retornar Optional.empty())

3. **listarPorRestaurante(Long restauranteId)**
   - Testar listagem de produtos por restaurante

4. **atualizar(Long id, Produto produtoAtualizado)**
   - Testar atualização bem-sucedida de um produto existente
   - Testar tentativa de atualização de produto inexistente (deve lançar IllegalArgumentException)

5. **alterarDisponibilidade(Long id, boolean disponivel)**
   - Testar alteração bem-sucedida da disponibilidade de um produto
   - Testar tentativa de alteração de produto inexistente (deve lançar IllegalArgumentException)

## RestauranteService

### Métodos a serem testados:

1. **cadastrar(Restaurante restaurante)**
   - Testar cadastro bem-sucedido de um novo restaurante
   - Testar tentativa de cadastro com nome já existente (deve lançar IllegalArgumentException)
   - Testar tentativa de cadastro com dados inválidos (nome vazio, taxa de entrega negativa)

2. **buscarPorId(Long id)**
   - Testar busca por ID existente (deve retornar o restaurante)
   - Testar busca por ID inexistente (deve retornar Optional.empty())

3. **listarAtivos()**
   - Testar listagem de restaurantes ativos

4. **atualizar(Long id, Restaurante restauranteAtualizado)**
   - Testar atualização bem-sucedida de um restaurante existente
   - Testar tentativa de atualização de restaurante inexistente (deve lançar IllegalArgumentException)
   - Testar tentativa de atualização com nome já existente (deve lançar IllegalArgumentException)

5. **inativar(Long id)**
   - Testar inativação bem-sucedida de um restaurante existente
   - Testar tentativa de inativação de restaurante inexistente (deve lançar IllegalArgumentException)

## Considerações sobre os testes

1. **Uso de mocks**: Utilizar Mockito para mockar as dependências (repositórios) e isolar a lógica de negócio.

2. **Cobertura de cenários**: Testar tanto os cenários felizes (operações bem-sucedidas) quanto os cenários de erro (lançamento de exceções).

3. **Verificação de interações**: Verificar se os métodos dos repositórios são chamados corretamente com os parâmetros esperados.

4. **Testes de validação**: Testar as validações de dados implementadas nos serviços.

5. **Testes de integração**: Além dos testes unitários, considerar a implementação de testes de integração para verificar a interação entre os componentes.