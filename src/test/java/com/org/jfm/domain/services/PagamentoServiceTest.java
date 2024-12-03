package com.org.jfm.domain.services;

import com.org.jfm.domain.entities.Pagamento;
import com.org.jfm.domain.ports.MercadoPagoApi;
import com.org.jfm.domain.ports.PagamentoRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private MercadoPagoApi mercadoPagoApi;

    @InjectMocks
    private PagamentoService pagamentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePagamento() {
        Pagamento pagamento = new Pagamento();
        pagamento.setPedidoID("12345");
        pagamento.setValue(100.50);
        pagamento.setStatus("pending");
        pagamento.setDateCreated(Instant.now().toString());

        when(pagamentoRepository.add(any(Pagamento.class))).thenReturn("60c72b2f9b1e8b3f4c8e4d3e");
        when(mercadoPagoApi.processPayment(anyString())).thenReturn("approved");

        String id = pagamentoService.createPagamento(pagamento);

        assertNotNull(id);
        assertEquals("approved", pagamento.getStatus());
        assertNotNull(pagamento.getDateApproved());
        verify(pagamentoRepository, times(1)).add(any(Pagamento.class));
        verify(pagamentoRepository, times(1)).updateStatus(anyString(), anyString(), anyString());
    }

    @Test
    void testGetPagamentoById() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(new ObjectId("60c72b2f9b1e8b3f4c8e4d3e"));
        pagamento.setPedidoID("12345");
        pagamento.setValue(100.50);
        pagamento.setStatus("approved");

        when(pagamentoRepository.findById(anyString())).thenReturn(pagamento);

        Pagamento result = pagamentoService.getPagamentoById("60c72b2f9b1e8b3f4c8e4d3e");

        assertNotNull(result);
        assertEquals("60c72b2f9b1e8b3f4c8e4d3e", result.getId().toHexString());
        verify(pagamentoRepository, times(1)).findById(anyString());
    }

    @Test
    void testUpdateStatus() {
        when(pagamentoRepository.updateStatus(anyString(), anyString(), anyString())).thenReturn(1L);

        long result = pagamentoService.updateStatus("60c72b2f9b1e8b3f4c8e4d3e", "approved");

        assertEquals(1L, result);
        verify(pagamentoRepository, times(1)).updateStatus(anyString(), anyString(), anyString());
    }

    @Test
    void testDeletePagamento() {
        when(pagamentoRepository.deletePagamento(anyString())).thenReturn(1L);

        long result = pagamentoService.deletePagamento("60c72b2f9b1e8b3f4c8e4d3e");

        assertEquals(1L, result);
        verify(pagamentoRepository, times(1)).deletePagamento(anyString());
    }
}