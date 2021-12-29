# Star Wars Resistence Social Network

Bem vindo.

Este app foi construído para a avaliação de desenvolimento na Let's Code;

**Descrição do problema:**
O império continua sua luta incessante de dominar a galáxia, tentando ao máximo expandir seu território e eliminar os rebeldes.
Você, como um soldado da resistência, foi designado para desenvolver um sistema para compartilhar recursos entre os rebeldes.

##### Critérios de Avaliação
- Deverá ser utilizado Java, Spring boot, Spring Data, Hibernate (pode ser usado o banco de dados H2) e como gerenciador de dependência Maven ou Gradle. 
- Não será necessário autenticação. 
- Nós ainda nos preocupamos com uma programação adequada (código limpo) e técnicas de
   arquitetura, você deve demonstrar que é um digno soldado da resistência através das suas
   habilidades. 
- Não esqueça de minimamente documentar os endpoints da sua API e como usa-los. 
- Sua API deve estar minimamente coberta por testes (Unitários e/ou integração). 
- Da descrição acima você pode escrever uma solução básica ou adicionar requisitos não descritos.
   Use seu tempo com sabedoria; Uma solução ótima e definitiva pode levar muito tempo para ser efetiva na guerra, então você deve trazer a melhor solução possível, que leve o mínimo de tempo, mas que ainda seja capaz de demonstrar suas habilidades e provar que você é um soldado valioso para a resistência. 
- Comente qualquer dúvida e cada decisão tomada.

## Desenvolvimento

### Tecnologia

- [X] JAVA
- [X] SpringBoot
- [X] Maven
- [X] Rest
- [X] Base H2
- [X] Lombok

Após as definições do Escopo tratadas no dia 17/12/2021. Iniciei o desenvolvimento apenas no dia 27/12/2021, gastando mais ou menos umas 6 Horas para tal e mais 6 horas no dia 28/12/2021. Nele está previsto a exposição de apis restful de modo a atender as seguintes requisições:

- Adicionar rebeldes
- Atualizar localização do rebelde
- Reportar o rebelde como um traidor 
  - Um rebelde é marcado como traidor quando, ao menos, três outros rebeldes reportarem a traição.
- Rebeldes não podem Adicionar/Remover itens do seu inventário
- Negociar itens
- Gerar relatórios
  - Porcentagem de traidores. 
  - Porcentagem de rebeldes. 
  - Quantidade média de cada tipo de recurso por rebelde (Ex: 2 armas por rebelde).
  - Pontos perdidos devido a traidores.

### Orientações para deploy e uso no ambiente de Desenvolvimento


## Subindo o Projeto

Para executar o projeto, execute o comando abaixo:

````
mvn clean package -DskipTests=true
java -jar -Dspring.profiles.active=local target/SocialNetwork-0.0.1-SNAPSHOT.jar
````

## APIS
- As documentações das apis podem ser vistas em http://localhost:8090/swagger-ui.html

### Adicionar Rebelde
#### POST - localhost:8090/rebel
Payload:
````
{
    "name": "Name Of Rebel",
    "age": 50,
    "gender": "MALE",
    "location": {
        "latitude": -27.631758,
        "longitude": -48.6737598,
        "name": "Galaxy Location"
    },
    "inventory": [
        {
            "quantity": 1,
            "type": "WEAPON"
        },
        {
            "quantity": 1,
            "type": "WATER"
        }
    ]

}
````

### Buscar Todos os Rebeldes
#### GET - localhost:8090/rebel

### Buscar um rebelde pelo ID
#### GET - localhost:8090/rebel/{id}

### Buscar a localização de um rebelde pelo ID
#### GET - localhost:8090/rebel/{id}/location

### Atualizar a localização de um rebelde pelo ID
#### PATCH - localhost:8090/rebel/{id}/location
Payload:
````
{
    "latitude": -27.631758,
    "longitude": -48.6737598,
    "name": "Galaxy Location"
}
````

### Verificar quantidades de vezes o rebelde foi reportado como traidor pelo ID
#### GET - localhost:8090/rebel/{id}/traitor

### Reportar rebelde como traidor pelo ID
#### PATCH - localhost:8090/rebel/{id}/traitor/report

### Trocar mercadorias entre usuários
#### POST - localhost:8090/trade
Payload:
````
{
    "traderOne": {
        "user": 1,
        "tradeResources": [
            {
                "quantity": 2,
                "type": "WEAPON"
            },
            {
                "quantity": 2,
                "type": "WATER"
            }
        ]
    },
    "traderTwo": {
        "user": 2,
        "tradeResources": [
            {
                "quantity": 3,
                "type": "FOOD"
            },
            {
                "quantity": 3,
                "type": "FOOD"
            }
        ]
    }
}
````

### Gerar relatórios de porcentagem de rebeldes
#### GET - localhost:8090/reports/rebels/percentage

### Gerar relatórios de porcentagem de traidores
#### GET - localhost:8090/reports/traitor/percentage

### Gerar relatórios de quantidade de pontos perdidos devido a traidores
#### GET - localhost:8090/reports/traitor/score/lost

### Gerar relatórios de quantidade média de cada tipo de recurso por rebelde
#### GET - localhost:8090/reports/resources/average

## Meta
Willian Robson Moraes 😜 – [LINKEDIN](https://www.linkedin.com/in/willmoraes) – <willian_200@hotmail.com>
