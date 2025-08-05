### `README.md`

# Projeto Lista de Tarefas e Eventos em Java

## 📜 Descrição do Projeto

Este é um sistema de gerenciamento de tarefas e eventos desenvolvido em Java, utilizando a biblioteca Swing para a interface gráfica de usuário (GUI) e Maven para o gerenciamento de dependências. O sistema permite que múltiplos usuários se cadastrem, façam login e gerenciem suas próprias listas de tarefas e eventos. Os dados são persistidos em arquivos JSON.

## ✨ Funcionalidades

  * **Gestão de Usuários**: Cadastro e autenticação de usuários com senha segura utilizando BCrypt.
  * **Gerenciamento de Tarefas**: Crie, edite e exclua tarefas, cada uma com título, descrição, prioridade e prazo.
  * **Gerenciamento de Subtarefas**: Adicione, edite e remova subtarefas para organizar tarefas maiores.
  * **Gerenciamento de Eventos**: Cadastre, edite e exclua eventos com título, descrição e prazo.
  * **Persistência de Dados**: As informações de usuários, tarefas e eventos são salvas em arquivos JSON para persistência entre sessões.
  * **Geração de Relatórios**: Capacidade de gerar relatórios em formato PDF e Excel (XLSX).
  * **Notificações por E-mail**: O sistema possui uma funcionalidade para enviar e-mails de notificação ao usuário sobre ações realizadas.

## 📂 Estrutura do Projeto

O projeto segue a estrutura padrão de projetos Maven. As classes principais e seus pacotes são:

  - `src/main/java/br/com/todolist/Main.java`: Ponto de entrada da aplicação.
  - `src/main/java/br/com/todolist/models`: Contém as classes de modelo de dados (ex: `Tarefa`, `Evento`, `Usuario`).
  - `src/main/java/br/com/todolist/service`: Contém as classes de lógica de negócios (Gerentes de Tarefas, Eventos, Usuários e o Orquestrador).
  - `src/main/java/br/com/todolist/persistence`: Contém a classe responsável pela persistência de dados em JSON.
  - `src/main/java/br/com/todolist/ui`: Contém as classes da interface gráfica de usuário (telas, painéis e diálogos).
  - `src/main/java/br/com/todolist/util`: Contém classes utilitárias para geração de relatórios e envio de e-mails.

## 🛠️ Tecnologias e Dependências

O projeto utiliza as seguintes bibliotecas, gerenciadas pelo Maven (`pom.xml`):

  * **Jackson**: `jackson-databind` e `jackson-datatype-jsr310` para serialização/desserialização de JSON.
  * **JBCrypt**: `org.mindrot:jbcrypt` para o hashing e verificação de senhas.
  * **iText**: `itext7-core` e `layout` para a criação de documentos PDF.
  * **Apache POI**: `poi-ooxml` para a manipulação de arquivos do Microsoft Office, como planilhas Excel.
  * **Jakarta Mail**: `jakarta.mail` para o envio de e-mails via SMTP.
  * **Junit**: `junit` para a realização de testes unitários.

## ▶️ Como Executar o Projeto

1.  Clone o repositório.
2.  Certifique-se de ter o Maven e um JDK 17 ou superior instalados.
3.  Navegue até a pasta raiz do projeto.
4.  Execute o comando Maven para rodar a classe principal:
    ```bash
    mvn clean install
    mvn compile exec:java
    ```
