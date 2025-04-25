# mini-authorizer-sb


suposicoes : 

1 - Não vale a pena criar a tabela de "User" nesse projeto, que seria o dono do cartão, pois provavelmente ja seria gerenciada por outra aplicacao.


2 - Desenvolvimento em inglês, mantendo em ptBR somente os termos em "contrato"


3 - Não foi citado o caso de transacao com valor negativo, que seria na prática um depósito no cartão. Tal operação tem suas particularidades e portanto deve ser tratada
separadamente. Para fins de simplificação, preferi lancar exception caso ocorra.


4 - Camada de Service (CardService, TransactionService) não coberta por testes simplesmente por falta de tempo. Seriam injetados os Services e os demais beans seriam mockados 
via Mockito. Seriam criados testes para validar os parâmetros, quantas vezes / se executou os demais mocks.


5 - Versionamento do db via Liquibase para facilitar / organizar


6 - Java 8 para ficar entre old school e 24
