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
1. Traduzir palavras desejadas.
2. Salvar traduções e exemplos no banco de dados.
3. Categorizar traduções como: Nova ou Revisão.
4. Realizar diferentes operações de busca:
   - Listar palavras
   - Listar frases
   - Filtrar por categoria
   - Buscar palavras específicas

## Pré-requisitos:
- Java JDK 21 instalado
- Maven instalado
- PostgreSQL instalado e em execução
- Uma IDE Java (ex: IntelliJ IDEA ou VS Code)

## Configurando o banco de dados

1. Acesse o arquivo `src/main/resources/application.properties`
2. Ajuste as propriedades `spring.datasource.username` e `spring.datasource.password` com suas credenciais do PostgreSQL
3. Crie um banco de dados no PostgreSQL e atualize a propriedade `spring.datasource.url` com o nome do banco criado.
4. Exemplo: `spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco`

## Criar uma API Key do Gemini

Usamos a API do Gemini, então é necessário gerar uma chave de acesso

1. Acesse o link: https://ai.google.dev/gemini-api/docs/api-key e crie sua chave
2. Você pode:
   - Adicionar a chave como variável de ambiente (recomendado), ou
   - Adicionar diretamente no código, no arquivo `GeminiAPI.java`:

```java
private final String API_KEY = System.getenv("GEMINI_API_KEY");
```

>💡 A API do Gemini é usada para gerar respostas com inteligência artificial no projeto.

## Como executar o projeto na sua máquina

1. Clone o repositório e acesse a pasta do projeto:
```bash
git clone https://github.com/NataliaFrancisca/javalingo.git
cd javalingo
```

2. Para executar o projeto:
   - **Via IDE**: abra o arquivo `src/main/java/br/com/nat/javalingo/JavalingoApplication.java` e clique no botão Run (disponível em IDEs como IntelliJ ou VS Code com extensão Java instalada)
   - **Via terminal**:
     ```mvn spring-boot:run```

## Atividades Pendentes:
- [X] criar flashcards para evoluir o nível de aprendizado de cada tradução.
- [ ] implementar funcionalidades de remoção de palavras e exemplos.
- [ ] criar testes unitários.

