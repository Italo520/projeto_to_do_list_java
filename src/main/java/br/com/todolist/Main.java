package br.com.todolist;

import br.com.todolist.repository.DatabaseManager;
import br.com.todolist.ui.telasusuario.TelaLogin;
import javax.swing.SwingUtilities;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;


public class Main {
    public static void main(String[] args) {
        // Initialize the DatabaseManager
        DatabaseManager.getEntityManagerFactory();

        FlatCarbonIJTheme.setup();

        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });

        // Add a shutdown hook to close the EntityManagerFactory
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseManager.close();
        }));
    }
}