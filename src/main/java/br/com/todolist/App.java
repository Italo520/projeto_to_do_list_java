package br.com.poo.todolist; // Pacote onde o arquivo App.java está

// --- IMPORTS NECESSÁRIOS ---
import br.com.poo.todolist.ui.frames.TelaPrincipal; // Importa sua tela principal
import javax.swing.*; // Importa todas as classes do Swing, incluindo JFrame e JMenuBar

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Gerenciador de Atividades");
        TelaPrincipal telaPrincipal = new TelaPrincipal();

        frame.setJMenuBar(telaPrincipal.criarBarraDeMenu());

        frame.setContentPane(telaPrincipal.getTelaPrincipalPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}