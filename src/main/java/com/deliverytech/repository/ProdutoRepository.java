package com.deliverytech.repository;

import com.deliverytech.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Adicione esta linha para corrigir o erro no service
    List<Produto> findByRestauranteId(Long restauranteId);
}
