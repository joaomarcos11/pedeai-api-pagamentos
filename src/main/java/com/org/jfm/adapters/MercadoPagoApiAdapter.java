package com.org.jfm.adapters;

import com.org.jfm.domain.ports.MercadoPagoApi;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Random;

@ApplicationScoped
public class MercadoPagoApiAdapter implements MercadoPagoApi {

    @Override
    public String processPayment(String paymentId) {

        Random random = new Random();
        boolean isApproved = random.nextBoolean();
        return isApproved ? "approved" : "canceled";
    }
}