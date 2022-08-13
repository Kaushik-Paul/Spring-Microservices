package com.example.customer.service;

import com.example.customer.entity.Customer;
import com.example.customer.entity.CustomerRegistrationRequest;
import com.example.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();

        // TODO: check if email is valid
        // TODO: check if email not taken
        customerRepository.save(customer);
    }
}
