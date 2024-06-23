package com.honeybadger.ecommerce.product;

import com.honeybadger.ecommerce.category.Category;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Product {
  @Id @GeneratedValue private Integer id;
  private String name;
  private String description;

  @Column(name = "avaialable_quantity")
  private double availableQuantity;

  private BigDecimal price;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;
}
