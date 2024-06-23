package com.honeybadger.ecommerce.payment;

import com.honeybadger.ecommerce.customer.CustomerResponse;
import com.honeybadger.ecommerce.order.PaymentMethod;
import java.math.BigDecimal;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Integer orderId,
    String orderReference,
    CustomerResponse customer) {}
