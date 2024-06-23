package com.honeybadger.ecommerce.notification;

import com.honeybadger.ecommerce.kafka.order.OrderConfirmation;
import com.honeybadger.ecommerce.kafka.payment.PaymentConfirmation;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {
  @Id private String id;
  private NotificationType notificationType;
  private LocalDateTime notificationDate;
  private OrderConfirmation orderConfirmation;
  private PaymentConfirmation paymentConfirmation;
}
