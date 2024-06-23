package com.honeybadger.ecommerce.product;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.honeybadger.ecommerce.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProductClient {
  private final RestTemplate restTemplate;

  @Value("${application.config.product-url}")
  private String productUrl;

  public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> requestBody) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    HttpEntity<List<PurchaseRequest>> reqHttpEntity = new HttpEntity<>(requestBody, headers);
    ParameterizedTypeReference<List<PurchaseResponse>> responseType =
        new ParameterizedTypeReference<>() {};
    ResponseEntity<List<PurchaseResponse>> responseEntity =
        restTemplate.exchange(productUrl + "/purchase", POST, reqHttpEntity, responseType);
    if(responseEntity.getStatusCode().isError()){
        throw  new BusinessException("An error occured while processing the products purchase: "+ responseEntity.getStatusCode());
    }
    return responseEntity.getBody();
  }
}
