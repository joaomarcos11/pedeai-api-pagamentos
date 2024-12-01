package com.org.jfm.controllers;

import com.org.jfm.domain.dto.PagamentoDTO;
import com.org.jfm.domain.entities.Pagamento;
import com.org.jfm.domain.services.PagamentoService;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PagamentoResourceTest {

    @Mock
    private PagamentoService pagamentoService;

    @InjectMocks
    private PagamentoResource pagamentoResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePagamento() {
        PagamentoDTO pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setPedidoID(12345L);
        pagamentoDTO.setValue(100.50);

        Pagamento pagamento = new Pagamento();
        pagamento.setId(new ObjectId());
        pagamento.setPedidoID(pagamentoDTO.getPedidoID());
        pagamento.setValue(pagamentoDTO.getValue());
        pagamento.setStatus("approved");
        pagamento.setDateCreated(Instant.now().toString());
        pagamento.setDateApproved(Instant.now().toString());

        when(pagamentoService.createPagamento(any(Pagamento.class))).thenAnswer(invocation -> {
            Pagamento p = invocation.getArgument(0);
            p.setId(new ObjectId()); // Ensure the ObjectId is set
            p.setStatus("approved");
            p.setDateApproved(Instant.now().toString());
            return p.getId().toHexString();
        });

        Response response = pagamentoResource.createPagamento(pagamentoDTO);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetPagamentoById() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(new ObjectId());
        pagamento.setPedidoID(12345L);
        pagamento.setValue(100.50);
        pagamento.setStatus("approved");

        when(pagamentoService.getPagamentoById(anyString())).thenReturn(pagamento);

        Response response = pagamentoResource.getPagamentoById(pagamento.getId().toHexString());

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Pagamento responseBody = (Pagamento) response.getEntity();
        assertEquals(pagamento.getId(), responseBody.getId());
    }

    @Test
    void testUpdateStatus() {
        String id = new ObjectId().toHexString();
        String status = "approved";

        when(pagamentoService.updateStatus(anyString(), anyString())).thenReturn(1L);

        Response response = pagamentoResource.updateStatus(id, status);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Map<String, String> responseBody = (Map<String, String>) response.getEntity();
        assertEquals("OK", responseBody.get("status"));
    }

    @Test
    void testDeletePagamento() {
        String id = new ObjectId().toHexString();

        when(pagamentoService.deletePagamento(anyString())).thenReturn(1L);

        Response response = pagamentoResource.deletePagamento(id);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Map<String, String> responseBody = (Map<String, String>) response.getEntity();
        assertEquals("OK", responseBody.get("status"));
    }
}