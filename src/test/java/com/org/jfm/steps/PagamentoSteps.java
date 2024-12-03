package com.org.jfm.steps;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.org.jfm.domain.dto.PagamentoDTO;
import com.org.jfm.domain.entities.Pagamento;
import com.org.jfm.domain.services.PagamentoService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoSteps {

    private WireMockServer wireMockServer;
    private PagamentoDTO pagamentoDTO;
    private Response response;
    private final String mockId = new ObjectId().toHexString();

    @Mock
    private PagamentoService pagamentoService;

    @Before
    public void setup() {
        wireMockServer = new WireMockServer(8081); // Use a different port
        wireMockServer.start();
        WireMock.configureFor("localhost", 8081);
        RestAssured.baseURI = "http://localhost:8081";
    }

    @After
    public void teardown() {
        wireMockServer.stop();
    }

    @Given("que eu tenho um pagamento válido")
    public void queEuTenhoUmPagamentoValido() {
        pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setPedidoID("67890");
        pagamentoDTO.setValue(100.50);

        // Configure mock response
        stubFor(post(urlEqualTo("/api/pagamento"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(String.format("{\"id\": \"%s\", \"status\": \"approved\", \"dateCreated\": \"2023-10-01T12:00:00Z\", \"dateApproved\": \"2023-10-02T12:00:00Z\"}", mockId))));
    }

    @When("eu envio uma requisição POST para {string}")
    public void euEnvioUmaRequisicaoPOSTPara(String endpoint) {
        response = RestAssured.given()
                .contentType("application/json")
                .body(pagamentoDTO)
                .post(endpoint);
    }

    @Then("eu devo receber uma resposta com status {string} ou {string}")
    public void euDevoReceberUmaRespostaComStatusOu(String status1, String status2) {
        String status = response.jsonPath().getString("status");
        Assertions.assertNotNull(status, "Status should not be null");
        Assertions.assertTrue(status.equals(status1) || status.equals(status2),
                "Status should be either " + status1 + " or " + status2);
    }

    @Then("a resposta deve conter o ID do pagamento")
    public void aRespostaDeveConterOIDDoPagamento() {
        String id = response.jsonPath().getString("id");
        Assertions.assertNotNull(id, "ID should not be null");
    }
}