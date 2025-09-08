# Documentação da API com Swagger

## Adicionando dependências do Swagger

Para adicionar documentação da API com Swagger ao projeto, é necessário incluir as seguintes dependências no `pom.xml`:

```xml
<!-- Springdoc OpenAPI UI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

## Configuração do Swagger

### 1. Criar classe de configuração

Criar uma classe de configuração para personalizar as informações da API:

```java
package com.deliverytech.delivery_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery API")
                        .version("1.0.0")
                        .description("API para gerenciamento de delivery de restaurantes")
                        .contact(new Contact()
                                .name("Delivery Tech")
                                .email("contato@deliverytech.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
```

### 2. Adicionar anotações nos controladores

Adicionar anotações do Swagger nos controladores para documentar os endpoints:

```java
package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Cadastrar novo cliente
     */
    @PostMapping
    @Operation(summary = "Cadastrar novo cliente", description = "Cria um novo cliente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", 
                    content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> cadastrar(
            @Parameter(description = "Dados do cliente a ser cadastrado") 
            @Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteSalvo = clienteService.cadastrar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    // ... outros métodos com anotações similares
}
```

### 3. Adicionar anotações nas entidades e DTOs

Adicionar anotações do Swagger nas entidades e DTOs para documentar os modelos:

```java
package com.deliverytech.delivery.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "clientes")
@Schema(description = "Representa um cliente do sistema de delivery")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do cliente", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nome completo do cliente", example = "João Silva", required = true)
    private String nome;

    @Column(nullable = false, unique = true)
    @Schema(description = "Email do cliente", example = "joao@example.com", required = true)
    private String email;

    @Column(nullable = false)
    @Schema(description = "Telefone do cliente", example = "(11) 99999-9999", required = true)
    private String telefone;

    @Schema(description = "Endereço do cliente", example = "Rua A, 123")
    private String endereco;

    @Column(nullable = false)
    @Schema(description = "Indica se o cliente está ativo", example = "true")
    private boolean ativo = true;

    // ... relacionamentos
}
```

```java
package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO usado para receber dados do cliente (entrada da API)
 */
@Data
@Schema(description = "DTO para dados de cliente")
public class ClienteDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Nome completo do cliente", example = "João Silva", required = true)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "Email do cliente", example = "joao@example.com", required = true)
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 8, max = 20, message = "Telefone deve ter entre 8 e 20 caracteres")
    @Schema(description = "Telefone do cliente", example = "(11) 99999-9999", required = true)
    private String telefone;

    @NotBlank(message = "Endereço é obrigatório")
    @Schema(description = "Endereço do cliente", example = "Rua A, 123", required = true)
    private String endereco;
}
```

## Acesso à documentação

Após a configuração, a documentação da API estará disponível em:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## Configurações adicionais

### Personalização da interface Swagger UI

Para personalizar a interface do Swagger UI, é possível adicionar propriedades ao `application.properties`:

```properties
# Swagger UI customização
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.docExpansion=none
```

### Agrupamento de APIs

Para agrupar as APIs por tags, é possível usar a anotação `@Tag` nos controladores:

```java
@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
public class ClienteController {
    // ...
}

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Operações relacionadas aos pedidos")
public class PedidoController {
    // ...
}

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
public class ProdutoController {
    // ...
}

@RestController
@RequestMapping("/restaurantes")
@Tag(name = "Restaurantes", description = "Operações relacionadas aos restaurantes")
public class RestauranteController {
    // ...
}
```

## Benefícios da documentação com Swagger

1. **Interface interativa**: Permite testar os endpoints diretamente pela interface web
2. **Documentação automática**: A documentação é gerada automaticamente a partir do código
3. **Padronização**: Facilita a padronização da documentação da API
4. **Integração com ferramentas**: Pode ser integrada com outras ferramentas de desenvolvimento
5. **Versionamento**: Permite versionar a documentação junto com a API