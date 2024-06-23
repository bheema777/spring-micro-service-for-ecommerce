package com.honeybadger.ecommerce.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
  List<Product> findAllByIdInOrderById(List<Integer> productIds);
}
