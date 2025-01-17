package com.honeybadger.ecommerce.orderline;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineService {
  private final OrderLineRepository repository;
  private final OrderLineMapper mapper;

  public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
    var order = mapper.toOrderLine(orderLineRequest);
    return repository.save(order).getId();
  }

  public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
    return repository.findAllByOrderId(orderId).stream().map(mapper::toOrderLineResponse).toList();
  }
}
