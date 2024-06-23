package com.honeybadger.ecommerce.order;

import static java.lang.String.format;

import com.honeybadger.ecommerce.customer.CustomerClient;
import com.honeybadger.ecommerce.exception.BusinessException;
import com.honeybadger.ecommerce.kafka.OrderConfirmation;
import com.honeybadger.ecommerce.kafka.OrderProducer;
import com.honeybadger.ecommerce.orderline.OrderLineRequest;
import com.honeybadger.ecommerce.orderline.OrderLineService;
import com.honeybadger.ecommerce.payment.PaymentClient;
import com.honeybadger.ecommerce.payment.PaymentRequest;
import com.honeybadger.ecommerce.product.ProductClient;
import com.honeybadger.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final CustomerClient customerClient;
  private final ProductClient productClient;
  private final OrderRepository orderRepository;
  private final OrderMapper mapper;
  private final OrderLineService orderLineService;
  private final OrderProducer orderProducer;
  private final PaymentClient paymentClient;

  @Transactional
  public Integer createOrder(OrderRequest request) {
    // check the customer
    var customer =
        this.customerClient
            .findCustomerById(request.customerId())
            .orElseThrow(
                () ->
                    new BusinessException(
                        "Cannot create order:: No Customer exists with the provided ID"));
    // purchase the products --> product micro-service (RestTemplate)
    var purchasedProducts = this.productClient.purchaseProducts(request.products());
    // persist order
    var order = this.orderRepository.save(mapper.toOrder(request));
    // persist order lines
    for (PurchaseRequest purchaseRequest : request.products()) {
      orderLineService.saveOrderLine(
          new OrderLineRequest(
              null, order.getId(), purchaseRequest.productId(), purchaseRequest.quantity()));
    }
    // TODO:: start payment process
    var paymentRequest =
        new PaymentRequest(
            request.amount(),
            request.paymentMethod(),
            order.getId(),
            order.getReference(),
            customer);
    paymentClient.requestOrderPayment(paymentRequest);
    // send the order confirmation --> notification micro-service (kafka)

    orderProducer.sendOrderConfirmation(
        new OrderConfirmation(
            request.reference(),
            request.amount(),
            request.paymentMethod(),
            customer,
            purchasedProducts));
    return order.getId();
  }

  public List<OrderResponse> findAll() {
    return orderRepository.findAll().stream().map(mapper::fromOrder).toList();
  }

  public OrderResponse findById(Integer orderId) {
    return orderRepository
        .findById(orderId)
        .map(mapper::fromOrder)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    format("No order found with the provided ID: %d", orderId)));
  }
}
