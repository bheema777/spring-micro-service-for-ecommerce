package com.honeybadger.ecommerce.category;

import com.honeybadger.ecommerce.product.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
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
public class Category {
  @Id @GeneratedValue private Integer id;
  private String name;
  private String description;

  @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
  private List<Product> products;
}
