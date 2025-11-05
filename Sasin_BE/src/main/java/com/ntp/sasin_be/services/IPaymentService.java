package com.ntp.sasin_be.services;

import com.ntp.sasin_be.dto.MomoPaymentInitResponse;

public interface IPaymentService {
    MomoPaymentInitResponse initiateMomoPayment(Long orderId);
    void handleMomoIpn(String rawBody);
}
