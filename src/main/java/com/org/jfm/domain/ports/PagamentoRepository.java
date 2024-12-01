package com.org.jfm.domain.ports;

import  com.org.jfm.domain.entities.Pagamento;

public interface PagamentoRepository {
    String add(Pagamento pagamento);
    Pagamento findById(String id);
    long updateStatus(String id, String status, String dateApproved);
    long deletePagamento(String id);
}