# 🪄 javalingo

Javalingo é um projeto que funciona como um dicionário e um livro digital para armazenar traduções e exemplos criados.

> 🚩 Atenção: as traduções são geradas por inteligência artificial, portanto, é importante revisar as respostas.

## Stack:
- Java
- Lambdas
- Spring Data JPA
- Maven
- PostgreSQL
- Gemini API

## Funcionalidades:
- Traduzir palavras desejadas.
-  Salvar traduções e exemplos no banco de dados.
- Categorizar traduções como: nova ou revisão.
- Realizar diferentes operações de busca: listar palavras e frases, filtrar por categoria e buscar palavras específicas.


## Pré-requisitos:
- Java JDK 21
- Maven
- PostgreSQL
- Uma IDE Java (ex: IntelliJ IDEA ou VS Code)


## Configurando o banco de dados
Crie um banco no PostgreSQL e depois altere os dados no arquivo `application.properties`

```
   spring.datasource.url=jdbc:mysql://localhost/nome-do-banco
   spring.datasource.username=seu-user-postgre
   spring.datasource.password=sua-senha-postgre

```


## Gerar chave no Gemini

>💡 A API do Gemini é usada para gerar as respostas, no caso as traduções e exemplos.

Para conseguir usar o Gemini precisamos de uma chave de acesso _"api key"_. Então acesse a página do [Gemini](https://ai.google.dev/gemini-api/docs/api-key) e gere uma chave.

No projeto essa chave pode estar em uma variável de ambiente ou você pode adicionar diretamente no código (`GeminiAPI.java`).
   ```java
      private final String API_KEY = System.getenv("GEMINI_API_KEY");
      // ou
      private final String API_KEY = "CHAVE-GERADA-PELO-GEMINI"
   ```


## Como executar o projeto na sua máquina:

Faça o clone desse repositório na sua máquina e acesse a pasta do projeto:

```bash
   git clone https://github.com/NataliaFrancisca/javalingo.git
   cd javalingo
```

Para executar o projeto:

**Via IDE**
- abra o arquivo: `src/main/java/br/com/nat/javalingo/JavalingoApplication.java`
- clique no botão Run (disponível em IDEs como IntelliJ ou VS Code com extensão Java instalada)

**Via terminal**: 
- rode o comando: `mvn spring-boot:run`

## Atividades pendentes:
- [X] criar flashcards para evoluir o nível de aprendizado de cada tradução.
- [ ] implementar funcionalidades de remoção de palavras e exemplos.
- [ ] criar testes unitários.

