package br.com.todolist;

import br.com.todolist.ui.telasusuario.TelaLogin;
import javax.swing.SwingUtilities;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

public class Main {
    public static void main(String[] args) {

        FlatCarbonIJTheme.setup();

        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}