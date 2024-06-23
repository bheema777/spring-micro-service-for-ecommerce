package com.honeybadger.ecommerce.product;

import com.honeybadger.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductMapper mapper;
  private final ProductRepository productRepository;

  public Integer createProduct(ProductRequest request) {
    var product = mapper.toProduct(request);
    return productRepository.save(product).getId();
  }

  public List<ProductPurchaseResponse> purchaseProduct(List<ProductPurchaseRequest> request) {
    var productIds = request.stream().map(ProductPurchaseRequest::productId).toList();
    var storedProducts = productRepository.findAllByIdInOrderById(productIds);
    if (productIds.size() != storedProducts.size()) {
      throw new ProductPurchaseException("One or More doesn't exist");
    }
    var storedRequest =
        request.stream().sorted(Comparator.comparing(ProductPurchaseRequest::productId)).toList();
    var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
    for (int i = 0; i < storedProducts.size(); i++) {
      var product = storedProducts.get(i);
      var productRequest = storedRequest.get(i);
      if (product.getAvailableQuantity() < productRequest.quantity()) {
        throw new ProductPurchaseException(
            "Insufficient stock quantity for product with ID ::" + productRequest.productId());
      }
      var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
      product.setAvailableQuantity(newAvailableQuantity);
      productRepository.save(product);
      purchasedProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
    }

    return purchasedProducts;
  }

  public ProductResponse findById(Integer productId) {
    return productRepository
        .findById(productId)
        .map(mapper::toProdcuctResponse)
        .orElseThrow(
            () -> new EntityNotFoundException("Product not found with the ID ::" + productId));
  }

  public List<ProductResponse> findAll() {
    return productRepository.findAll().stream().map(mapper::toProdcuctResponse).toList();
  }
}
