package com.org.jfm.domain.ports;

public interface MercadoPagoApi {
    String processPayment(String paymentId);
}