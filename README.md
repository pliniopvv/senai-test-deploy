# LABMedical

## Descrição:
LABMedical é um sistema de gestão para instituições de saúde, projetado para simplificar o processo de registro de pacientes e a gestão de exames e consultas. O presente projeto compões a API REST do sistema, responsável por gerenciar os dados e fornecer informações para o frontend.

Este projeto foi desenvolvido como parte da formação fullstack do programa Floripa Mais Tec, oferecida pelo LAB365/SENAI (Florianópolis, Brasil).

### Equipe: Qcode

- [Felipe Quérette](https://www.linkedin.com/in/felipe-querette/)
- [Pedro Xavier](https://www.linkedin.com/in/xavierpedroo/)
- [Plínio Victor Vianna](https://devplenio.com.br/)
- [Rosa Cristina Freitas](https://www.linkedin.com/in/cristina-freitas-fln/)

## Desenvolvimento

O projeto foi desenvolvido utilizando um board do kanban no Trello e versionamento de código no GitHub.

- [Board no Trello](https://trello.com/b/2ToydGS8/m3p-backend-squad-2)

## Tecnologias utilizadas:

- Java 17
- Spring Boot 3.3.5
- Maven
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Swagger
- JUnit
- Mockito
- H2
- Docker
- GitHub
- Jacoco

## Como executar?

1. Clone o repositório;
2. Configure o arquivo `application.properties` com as informações do banco de dados;
3. Execute o arquivo `keys.bat` ou `keys.sh` para gerar os certificados públicos e privados na pasta `resources`;
4. Executar o seguinte comando: `mvn spring-boot:run`

## Documentação:

A documentação da API pode ser acessada em `http://localhost:8081/swagger-ui/index.html#/`

