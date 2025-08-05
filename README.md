### `README.md`

# Projeto Lista de Tarefas e Eventos em Java

## 📜 Descrição do Projeto

Este é um sistema de gerenciamento de tarefas e eventos desenvolvido em Java, utilizando a biblioteca Swing para a interface gráfica de usuário (GUI) e Maven para o gerenciamento de dependências. O sistema permite que múltiplos usuários se cadastrem, façam login e gerenciem suas próprias listas de tarefas e eventos. Os dados são persistidos em arquivos JSON.

## ✨ Funcionalidades
* **Gestão de Usuários**: Cadastro e autenticação de usuários com senha segura utilizando BCrypt.
* **Gerenciamento de Tarefas**: Crie, edite e exclua tarefas, cada uma com título, descrição, prioridade e prazo. O progresso da tarefa é calculado dinamicamente com base em suas subtarefas ou na data de conclusão, caso não haja subtarefas.
* **Gerenciamento de Subtarefas**: Adicione, edite e remova subtarefas para organizar tarefas maiores. O status de conclusão de cada subtarefa é controlado individualmente.
* **Gerenciamento de Eventos**: Cadastre, edite e exclua eventos com título, descrição e prazo. O sistema exibe quantos dias faltam para a data limite de cada evento.
* **Persistência de Dados**: As informações de usuários, tarefas e eventos são salvas em um único arquivo JSON (`dados_globais.json`) para persistência entre sessões. O projeto foi refatorado para usar a biblioteca Jackson para serialização e desserialização de forma mais robusta e moderna.
* **Geração de Relatórios**: Capacidade de gerar relatórios em formato PDF e Excel (XLSX) utilizando as bibliotecas iText e Apache POI.
* **Notificações por E-mail**: O sistema possui uma funcionalidade para enviar e-mails de notificação ao usuário sobre ações realizadas, como criação, edição e exclusão de tarefas e eventos.


## 📂 Estrutura do Projeto

O projeto segue a estrutura padrão de projetos Maven. As classes principais e seus pacotes são:

* `src/main/java/br/com/todolist/Main.java`: Ponto de entrada da aplicação.
* `src/main/java/br/com/todolist/models`: Contém as classes de modelo de dados (ex: `Tarefa`, `Evento`, `Usuario`).
* `src/main/java/br/com/todolist/service`: Contém as classes de lógica de negócios (Gerentes de Tarefas, Eventos, Usuários e o Orquestrador).
* `src/main/java/br/com/todolist/persistence`: Contém a classe responsável pela persistência de dados em JSON.
* `src/main/java/br/com/todolist/ui`: Contém as classes da interface gráfica de usuário (telas, painéis e diálogos).
* `src/main/java/br/com/todolist/util`: Contém classes utilitárias para geração de relatórios e envio de e-mails.

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


### Detalhamento dos Arquivos

#### **Camada de Modelos (`br.com.todolist.models`)**

* **`Itens.java`**
    * **Função:** Classe abstrata que serve como base para `Tarefa` e `Evento`.
    * **Métodos e Atributos:**
        * Atributos `titulo`, `descricao`, `tipo`, `criado_por`, `dataCadastro` e `deadline`.
        * Possui um construtor que inicializa todos os atributos, exceto `tipo`, que é definido nas classes filhas.
        * Possui construtor vazio, necessário para a desserialização do Jackson.
        * Métodos `get...()` e `set...()` padrão.
    * **Integração:** Garante que todos os itens do sistema tenham um conjunto mínimo de informações, promovendo a reutilização de código e mantendo a estrutura consistente.

* **`Tarefa.java`**
    * **Função:** Representa uma tarefa. Estende `Itens` e adiciona lógica de gerenciamento de progresso e subtarefas.
    * **Métodos Principais:**
        * **`getPercentual()`**: **Método calculado.** Retorna o percentual de conclusão. Se a tarefa não tiver subtarefas, o valor é 100% se a `dataConclusao` não for nula e 0% caso contrário. Se tiver subtarefas, o progresso é calculado pela porcentagem de subtarefas concluídas.
        * **`adicionarSubtarefa(Subtarefa subtarefa)`**: Adiciona uma subtarefa à lista.
        * **`removerSubtarefa(Subtarefa subtarefa)`**: Remove uma subtarefa.
    * **Integração:** A anotação `@JsonIgnoreProperties(ignoreUnknown = true)` garante a compatibilidade com versões antigas do JSON que continham o campo `percentual` fixo.

* **`Evento.java`**
    * **Função:** Representa um evento. Estende `Itens`.
    * **Métodos Principais:**
        * **`toString()`**: Retorna uma representação em texto do evento, exibindo o título e o tempo restante para o prazo.
    * **Integração:** Armazena informações de eventos e oferece uma representação textual dinâmica.

* **`Subtarefa.java`**
    * **Função:** Representa uma subtarefa de uma `Tarefa`.
    * **Métodos Principais:**
        * **`mudarStatus()`**: Altera o status de conclusão.
        * **`isStatus()`**: Retorna o estado de conclusão.
    * **Integração:** Um modelo simples que compõe a classe `Tarefa`.

* **`Usuario.java`**
    * **Função:** Representa um usuário do sistema.
    * **Métodos Principais:**
        * **`Usuario()`**: Construtor padrão, essencial para a desserialização do Jackson.
        * **`Usuario(String nome, String email, String password)`**: Construtor que inicializa o objeto.
    * **Integração:** A entidade fundamental para a segurança e autenticação do sistema.

---

### **Camada de Serviços (`br.com.todolist.service`)**

* **`Orquestrador.java`**
    * **Função:** É a classe principal de serviço, coordenando as ações e integrando a funcionalidade de envio de e-mail.
    * **Métodos Principais:**
        * **`Orquestrador(Usuario usuario)`**: Inicializa os gerentes de dados e de e-mail, passando o `emailUsuario` para os gerentes de tarefas e eventos.
        * **`cadastrarTarefa(...)` e `cadastrarEvento(...)`**: Criam novos itens, preenchem o campo `criado_por` e enviam uma notificação por e-mail.
    * **Integração:** Centraliza as chamadas para as regras de negócio e adiciona a funcionalidade de notificação por e-mail.

* **`GerenteDeUsuarios.java`**
    * **Função:** Gerencia a persistência e a lógica de autenticação dos usuários.
    * **Métodos Principais:**
        * **`criarNovoUsuario(...)`**: Adiciona um novo usuário, hasheando a senha com BCrypt e salvando os dados.
        * **`autenticarUsuario(...)`**: Verifica a senha fornecida com o hash salvo.
        * **`verificarSenha(...)`**: Compara uma senha com o hash salvo.
    * **Integração:** É a camada de segurança do sistema.

* **`GerenteDeDadosDoUsuario.java`**
    * **Função:** Gerencia a leitura e escrita de dados em um arquivo global (`dados_globais.json`).
    * **Métodos Principais:**
        * **`salvarDados()`**: Salva todas as listas de tarefas e eventos no arquivo.
        * **`carregarDados()`**: Carrega todos os dados do arquivo para as listas.
    * **Integração:** Centraliza todos os dados de tarefas e eventos para que os gerentes específicos possam fazer a filtragem por usuário.

* **`DadosUsuario.java`**
    * **Função:** Classe DTO que define a estrutura do arquivo `dados_globais.json`.
    * **Atributos:** `tarefas` e `eventos`.
    * **Integração:** Facilita a serialização e desserialização de múltiplos tipos de objetos com o Jackson.

* **`GerenteDeTarefas.java`**
    * **Função:** Lógica de negócio para tarefas, incluindo a filtragem por usuário.
    * **Métodos Principais:**
        * **Construtor `GerenteDeTarefas(...)`**: Recebe o e-mail do usuário logado para realizar a filtragem.
        * **`listarTodasTarefas()`**: Retorna a lista de tarefas, filtrando-as pelo `emailUsuario` logado.
        * **`cadastrarTarefa(...)`**: Adiciona uma tarefa após verificar se ela pertence ao usuário logado.
    * **Integração:** Garante que cada usuário acesse e modifique apenas suas próprias tarefas.

* **`GerenteDeEventos.java`**
    * **Função:** Lógica de negócio para eventos, com filtragem por usuário e validação de datas.
    * **Métodos Principais:**
        * **`cadastrarEvento(...)`**: Adiciona um evento após verificar se o `deadline` não está ocupado por outro evento do mesmo usuário.
        * **`listarTodosEventos()`**: Retorna a lista de eventos, filtrada pelo `emailUsuario` logado.
    * **Integração:** Assegura que a lógica de negócios para eventos seja segura e consistente.

---

### **Camada de Interface de Usuário e Utilitários**

* **`GerenciadorDePersistenciaJson.java`**
    * **Função:** Responsável por ler e escrever dados em arquivos JSON.
    * **Métodos Principais:**
        * **`salvar(Object objeto)`**: Serializa um objeto Java e o salva em um arquivo JSON.
        * **`carregar(Type tipoDeDados)`**: Desserializa um arquivo JSON para um objeto Java.
    * **Integração:** Fornece a base para o salvamento e carregamento de dados em disco.

* **`Mensageiro.java`**
    * **Função:** Classe utilitária para envio de e-mails.
    * **Métodos Principais:**
        * **`enviarEmail(...)`**: Envia um e-mail simples para um destinatário.
    * **Integração:** Utilizada pelo `Orquestrador` para enviar notificações por e-mail.
