package br.com.todolist;

// A linha mais importante para resolver o erro:
import br.com.todolist.ui.telasusuario.TelaLogin;

/**
 * Ponto de entrada principal da aplicação.
 */
public class App {
    public static void main(String[] args) {
        // A aplicação agora inicia pela tela de login.
        // A linha abaixo (provavelmente a linha 9) é a que causa o erro se o import faltar.
        new TelaLogin().setVisible(true);
    }
}