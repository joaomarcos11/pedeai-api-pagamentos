package com.org.jfm.config;

import com.org.jfm.domain.ports.MercadoPagoApi;
import com.org.jfm.domain.ports.PagamentoRepository;
import com.org.jfm.domain.services.PagamentoService;
import org.mockito.Mockito;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@ApplicationScoped
public class TestConfig {

    @Produces
    @Singleton
    public PagamentoRepository pagamentoRepository() {
        return Mockito.mock(PagamentoRepository.class);
    }

    @Produces
    @Singleton
    public MercadoPagoApi mercadoPagoApi() {
        return Mockito.mock(MercadoPagoApi.class);
    }

    @Produces
    @Singleton
    public PagamentoService pagamentoService(PagamentoRepository pagamentoRepository, MercadoPagoApi mercadoPagoApi) {
        return new PagamentoService(pagamentoRepository, mercadoPagoApi);
    }
}