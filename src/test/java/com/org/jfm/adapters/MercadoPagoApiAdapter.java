package com.org.jfm.adapters;

import com.org.jfm.domain.ports.MercadoPagoApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MercadoPagoApiAdapterTest {

    private MercadoPagoApi mercadoPagoApi;

    @BeforeEach
    void setUp() {
        mercadoPagoApi = Mockito.mock(MercadoPagoApi.class);
    }

    @Test
    void testProcessPayment() {
        String paymentId = "60c72b2f9b1e8b3f4c8e4d3e";
        when(mercadoPagoApi.processPayment(paymentId)).thenReturn("approved");

        String status = mercadoPagoApi.processPayment(paymentId);

        assertNotNull(status);
        assertEquals("approved", status);
        verify(mercadoPagoApi).processPayment(paymentId);
    }
}