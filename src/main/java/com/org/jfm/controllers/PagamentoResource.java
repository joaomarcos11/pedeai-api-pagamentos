package com.org.jfm.controllers;

import com.org.jfm.domain.dto.PagamentoDTO;
import com.org.jfm.domain.entities.Pagamento;
import com.org.jfm.domain.services.PagamentoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Pagamento", description = "Operações de Pagamento")
public class PagamentoResource {

    @Inject
    PagamentoService pagamentoService;

    @GET
    @Path("/hello")
    @Operation(summary = "Endpoint de Hello", description = "Retorna uma mensagem de hello")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Mensagem de hello")
    })
    public Response hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from Quarkus REST");
        return Response.ok(response).build();
    }

    @POST
    @Path("/pagamento")
    @Operation(summary = "Criar Pagamento", description = "Cria um novo Pagamento")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Pagamento criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    public Response createPagamento(PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = new Pagamento();
        pagamento.setPedidoID(pagamentoDTO.getPedidoID());
        pagamento.setValue(pagamentoDTO.getValue());
        pagamento.setStatus("pending");

        String id = pagamentoService.createPagamento(pagamento);
        Map<String, String> response = new HashMap<>();
        response.put("id", id);
        response.put("status", pagamento.getStatus());
        response.put("dateCreated", pagamento.getDateCreated());
        response.put("dateApproved", pagamento.getDateApproved());
        return Response.ok(response).build();
    }

    @GET
    @Path("/pagamento/{id}")
    @Operation(summary = "Obter Pagamento por ID", description = "Retorna um Pagamento pelo ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Pagamento encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pagamento.class)))
    })
    public Response getPagamentoById(@PathParam("id") String id) {
        Pagamento pagamento = pagamentoService.getPagamentoById(id);
        if (pagamento == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(pagamento).build();
    }

    @PUT
    @Path("/pagamento/{id}")
    @Operation(summary = "Atualizar status do Pagamento", description = "Atualiza o status de um Pagamento")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Status do Pagamento atualizado")
    })
    public Response updateStatus(@PathParam("id") String id, @QueryParam("status") String status) {
        pagamentoService.updateStatus(id, status);
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/pagamento/{id}")
    @Operation(summary = "Deletar Pagamento", description = "Deleta um Pagamento")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Pagamento deletado")
    })
    public Response deletePagamento(@PathParam("id") String id) {
        pagamentoService.deletePagamento(id);
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        return Response.ok(response).build();
    }
}