package com.honeybadger.ecommerce.customer;

import static java.lang.String.format;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepository repository;
  private final CustomerMapper mapper;

  public String createCustomer(CustomerRequest request) {
    var customer = repository.save(mapper.toCustomer(request));
    return customer.getId();
  }

  public void updateCustomer(CustomerRequest request) {
    var customer =
        repository
            .findById(request.id())
            .orElseThrow(
                () ->
                    new CustomerNotFoundException(
                        format(
                            "Cannot update customer :: No customer found with the provided ID:: %s",
                            request.id())));
    mergerCustomer(customer, request);
    repository.save(customer);
  }

  public List<CustomerResponse> findAllCustomer() {
    return repository.findAll().stream().map(mapper::fromCustomer).toList();
  }

  public Boolean existsById(String customerId) {
    return repository.findById(customerId).isPresent();
  }

  public CustomerResponse findById(String customerId) {
    return repository
        .findById(customerId)
        .map(mapper::fromCustomer)
        .orElseThrow(
            () ->
                new CustomerNotFoundException(
                    format(
                        "Cannot update customer :: No customer found with the provided ID:: %s",
                        customerId)));
  }

  public void deleteCustomer(String customerId) {
    repository.deleteById(customerId);
  }

  private void mergerCustomer(Customer customer, CustomerRequest request) {
    if (StringUtils.isNotBlank(request.firstName())) {
      customer.setFirstName(request.firstName());
    }
    if (StringUtils.isNotBlank(request.lastName())) {
      customer.setLastName(request.lastName());
    }
    if (StringUtils.isNotBlank(request.email())) {
      customer.setEmail(request.email());
    }
    if (Objects.nonNull(request.address())) {
      customer.setAddress(request.address());
    }
  }
}
