package com.payhint.api.application.crm.usecase;

import java.util.List;

import com.payhint.api.application.crm.dto.request.CreateCustomerRequest;
import com.payhint.api.application.crm.dto.request.UpdateCustomerRequest;
import com.payhint.api.application.crm.dto.response.CustomerResponse;
import com.payhint.api.domain.crm.valueobject.CustomerId;
import com.payhint.api.domain.crm.valueobject.UserId;

public interface CustomerManagementUseCase {
    CustomerResponse viewCustomerProfile(UserId userId, CustomerId customerId);

    List<CustomerResponse> listAllCustomers(UserId userId);

    CustomerResponse createCustomer(UserId userId, CreateCustomerRequest request);

    CustomerResponse updateCustomerDetails(UserId userId, CustomerId customerId, UpdateCustomerRequest request);

    void deleteCustomer(UserId userId, CustomerId customerId);
}
