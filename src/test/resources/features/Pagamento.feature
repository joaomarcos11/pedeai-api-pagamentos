Feature: Pagamento

  Scenario: Criar um novo pagamento
    Given que eu tenho um pagamento válido
    When eu envio uma requisição POST para "/api/pagamento"
    Then eu devo receber uma resposta com status "approved" ou "pending"
    And a resposta deve conter o ID do pagamento