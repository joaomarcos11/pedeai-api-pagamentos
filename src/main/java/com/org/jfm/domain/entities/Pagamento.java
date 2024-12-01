package com.org.jfm.domain.entities;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@BsonDiscriminator(key = "_t", value = "com.org.jfm.domain.entities")
@Schema(description = "Representa um pagamento")
public class Pagamento {
    @BsonId
    @Schema(description = "ID do pagamento", example = "60c72b2f9b1e8b3f4c8e4d3e")
    private ObjectId id;

    @BsonProperty("pedidoID")
    @Schema(description = "ID do pedido", example = "12345")
    private Long pedidoID;

    @BsonProperty("value")
    @Schema(description = "Valor do pagamento", example = "100.50")
    private Double value;

    @BsonProperty("status")
    @Schema(description = "Status do pagamento", example = "pending")
    private String status;

    @BsonProperty("dateCreated")
    @Schema(description = "Data de criação do pagamento", example = "2023-10-01T12:00:00Z")
    private String dateCreated;

    @BsonProperty("dateApproved")
    @Schema(description = "Data de aprovação do pagamento", example = "2023-10-02T12:00:00Z")
    private String dateApproved;

    // Getters and setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getPedidoID() {
        return pedidoID;
    }

    public void setPedidoID(Long pedidoID) {
        this.pedidoID = pedidoID;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(String dateApproved) {
        this.dateApproved = dateApproved;
    }
}