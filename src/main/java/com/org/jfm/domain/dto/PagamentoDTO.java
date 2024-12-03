package com.org.jfm.domain.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "DTO for creating a new Pagamento")
public class PagamentoDTO {
    @Schema(description = "ID do pedido", example = "12345")
    private String pedidoID;

    @Schema(description = "Valor do pagamento", example = "100.50")
    private Double value;

    public String getPedidoID() {
        return pedidoID;
    }

    public void setPedidoID(String pedidoID) {
        this.pedidoID = pedidoID;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}