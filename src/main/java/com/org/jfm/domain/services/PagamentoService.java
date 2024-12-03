package com.org.jfm.domain.services;

import com.org.jfm.domain.entities.Pagamento;
import com.org.jfm.domain.ports.MercadoPagoApi;
import com.org.jfm.domain.ports.PagamentoRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;

import org.bson.types.ObjectId;

@ApplicationScoped
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final MercadoPagoApi mercadoPagoApi;

    @Inject
    public PagamentoService(PagamentoRepository pagamentoRepository, MercadoPagoApi mercadoPagoApi) {
        this.pagamentoRepository = pagamentoRepository;
        this.mercadoPagoApi = mercadoPagoApi;
    }

    public String createPagamento(Pagamento pagamento) {
        pagamento.setId(new ObjectId());
        pagamento.setDateCreated(Instant.now().toString());
        String id = pagamentoRepository.add(pagamento);

        String status = mercadoPagoApi.processPayment(id);
        String dateApproved = Instant.now().toString();
        pagamento.setStatus(status);
        pagamento.setDateApproved(dateApproved);
        pagamentoRepository.updateStatus(id, status, dateApproved);
        System.out.println("Pagamento aprovado: " + id);
        return id;
    }

    public Pagamento getPagamentoById(String id) {
        return pagamentoRepository.findById(id);
    }

    public long updateStatus(String id, String status) {
        String dateApproved = Instant.now().toString();
        return pagamentoRepository.updateStatus(id, status, dateApproved);
    }

    public long deletePagamento(String id) {
        return pagamentoRepository.deletePagamento(id);
    }
}