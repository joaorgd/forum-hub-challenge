FórumHub API

📖 Descrição
FórumHub é uma API REST para um fórum de discussões, desenvolvida como parte do Challenge Back-End da Alura em parceria com a Oracle (ONE). O projeto simula o back-end de um fórum, focando na administração de tópicos, onde os usuários podem criar, visualizar, atualizar e deletar posts. A API foi construída seguindo as melhores práticas do modelo REST, com um sistema de autenticação e autorização robusto para proteger os endpoints.

🚀 Funcionalidades Principais
A API FórumHub oferece as seguintes funcionalidades:

Autenticação de Usuários:

Endpoint de login (/login) que autentica usuários e retorna um token JWT para acesso seguro.

Gerenciamento de Tópicos (CRUD):

POST /topicos: Cadastrar um novo tópico (requer autenticação).

GET /topicos: Listar todos os tópicos de forma paginada e ordenada.

GET /topicos/{id}: Detalhar um tópico específico pelo seu ID.

PUT /topicos: Atualizar as informações de um tópico existente.

DELETE /topicos/{id}: Excluir um tópico.

Regras de Negócio:

Validação para impedir o cadastro de tópicos duplicados (mesmo título e mensagem).

Controle de acesso para garantir que apenas o autor de um tópico possa atualizá-lo ou excluí-lo.

🛠️ Tecnologias Utilizadas
Este projeto foi desenvolvido com as seguintes tecnologias e ferramentas:

Linguagem: Java 17

Framework: Spring Boot 3

Segurança: Spring Security 6 com autenticação via Token JWT (JSON Web Token)

Persistência de Dados: Spring Data JPA / Hibernate

Banco de Dados: PostgreSQL

Versionamento de Banco de Dados: Flyway

Gerenciamento de Dependências: Maven

Validações: Spring Validation

⚙️ Como Executar o Projeto
Siga os passos abaixo para executar a aplicação localmente.

Pré-requisitos
Antes de começar, você vai precisar ter instalado em sua máquina:

JDK 17 ou superior.

Maven 3.8 ou superior.

PostgreSQL (ou Docker com uma imagem do PostgreSQL).

Uma IDE de sua preferência (ex: IntelliJ IDEA).

Git.

1. Clone o Repositório
   git clone https://github.com/seu-usuario/forum-hub-challenge.git
   cd forum-hub-challenge

2. Configure o Banco de Dados
   Abra seu cliente PostgreSQL.

Crie um novo banco de dados para a aplicação:

CREATE DATABASE forumhub;

3. Configure as Variáveis de Ambiente
   A aplicação utiliza o arquivo src/main/resources/application.properties para as configurações. As credenciais do banco de dados e o segredo do JWT devem ser configurados.

# application.properties

# URL de conexão com o PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/forumhub

# Credenciais do banco de dados (use variáveis de ambiente em produção)
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:sua_senha}

# Chave secreta para assinar os tokens JWT
api.security.token.secret=seu-secret-super-secreto-para-desenvolvimento

Substitua sua_senha pela senha do seu usuário do PostgreSQL.

4. Execute a Aplicação
   Você pode executar a aplicação diretamente pela sua IDE ou via linha de comando com o Maven:

./mvnw spring-boot:run

A API estará disponível em http://localhost:8080. O Flyway irá criar automaticamente as tabelas usuarios e topicos na primeira inicialização.

🕹️ Como Usar a API
Para interagir com os endpoints protegidos, você primeiro precisa se autenticar.

1. Crie um Usuário de Teste
   Insira um usuário no banco com uma senha criptografada em BCrypt.

-- Use um gerador BCrypt para a senha (ex: "123456")
INSERT INTO usuarios (nome, email, senha)
VALUES ('Usuário de Teste', 'teste@email.com', '$2a$10$...hash_gerado_pelo_bcrypt...');

2. Obtenha o Token de Autenticação
   Envie uma requisição POST para http://localhost:8080/login com o seguinte corpo:

{
"email": "teste@email.com",
"senha": "123456"
}

A resposta será um JSON contendo o token JWT:

{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}

3. Faça Requisições Autenticadas
   Para acessar qualquer outro endpoint, adicione o token no cabeçalho Authorization da sua requisição:

Header: Authorization
Value: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

👨‍💻 Autor
Desenvolvido por João Roberto.