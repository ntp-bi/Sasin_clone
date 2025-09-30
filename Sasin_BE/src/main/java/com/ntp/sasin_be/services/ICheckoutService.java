package com.ntp.sasin_be.services;

import com.ntp.sasin_be.dto.CheckoutRequest;
import com.ntp.sasin_be.dto.OrderDTO;

public interface ICheckoutService {
    OrderDTO checkout(CheckoutRequest request);
}
