package com.honeybadger.ecommerce.kafka;

import static java.lang.String.*;

import com.honeybadger.ecommerce.email.EmailService;
import com.honeybadger.ecommerce.kafka.order.OrderConfirmation;
import com.honeybadger.ecommerce.kafka.payment.PaymentConfirmation;
import com.honeybadger.ecommerce.notification.Notification;
import com.honeybadger.ecommerce.notification.NotificationRepository;
import com.honeybadger.ecommerce.notification.NotificationType;
import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
  private final NotificationRepository repository;

  private final EmailService emailService;

  @KafkaListener(topics = "payment-topic")
  public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation)
      throws MessagingException {
    log.info(format("Consuming the message from payment-topic Topic :: %s", paymentConfirmation));
    repository.save(
        Notification.builder()
            .notificationType(NotificationType.PAYMENT_CONFIRMATION)
            .notificationDate(LocalDateTime.now())
            .paymentConfirmation(paymentConfirmation)
            .build());
    var customerName =
        paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName();
    emailService.sendPaymentSuccessEmail(
        paymentConfirmation.customerEmail(),
        customerName,
        paymentConfirmation.amount(),
        paymentConfirmation.orderReference());
  }

  @KafkaListener(topics = "order-topic")
  public void consumeOrderConfirmationNotification(OrderConfirmation orderConfirmation)
      throws MessagingException {
    log.info(format("Consuming the message from order-topic Topic :: %s", orderConfirmation));
    repository.save(
        Notification.builder()
            .notificationType(NotificationType.ORDER_CONFIRMATION)
            .notificationDate(LocalDateTime.now())
            .orderConfirmation(orderConfirmation)
            .build());
    var customerName =
        orderConfirmation.customer().firstname() + " " + orderConfirmation.customer().lastname();
    emailService.sendOrderConfirmationEmail(
        orderConfirmation.customer().email(),
        customerName,
        orderConfirmation.totalAmount(),
        orderConfirmation.orderReference(),
        orderConfirmation.products());
  }
}
