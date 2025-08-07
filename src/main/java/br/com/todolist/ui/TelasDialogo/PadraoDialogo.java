// Em: src/main/java/br/com/todolist/ui/PadraoDialogo.java
package br.com.todolist.ui.TelasDialogo;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;


public final class PadraoDialogo {

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private PadraoDialogo() {}

    public static Optional<LocalDate> pedirData(JFrame owner, String titulo, String mensagem) {
        LocalDate dataSelecionada = null;
        while (dataSelecionada == null) {
            String input = JOptionPane.showInputDialog(owner, mensagem, titulo, JOptionPane.QUESTION_MESSAGE);


            if (input == null) {
                return Optional.empty();
            }

            try {
                dataSelecionada = LocalDate.parse(input.trim(), FORMATADOR_DATA);
            } catch (DateTimeParseException e) {
                mostrarMensagemErro(owner, "Formato de data inválido. Por favor, use dd/MM/yyyy.");

            }
        }
        return Optional.of(dataSelecionada);
    }

    public static Optional<String> pedirTexto(JFrame owner, String titulo, String mensagem) {
        String input = JOptionPane.showInputDialog(owner, mensagem, titulo, JOptionPane.QUESTION_MESSAGE);
        

        if (input != null && !input.trim().isEmpty()) {
            return Optional.of(input.trim());
        }
        return Optional.empty();
    }
    

    public static boolean confirmarAcao(JFrame owner, String titulo, String mensagem) {
        int resultado = JOptionPane.showConfirmDialog(owner, mensagem, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return resultado == JOptionPane.YES_OPTION;
    }


    public static void mostrarMensagemErro(JFrame owner, String mensagem) {
        JOptionPane.showMessageDialog(owner, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    

    public static void mostrarMensagemInfo(JFrame owner, String mensagem) {
        JOptionPane.showMessageDialog(owner, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

  
    public static void mostrarResultados(JFrame owner, String titulo, String conteudo) {
        JTextArea areaDeTexto = new JTextArea(conteudo);
        areaDeTexto.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaDeTexto);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        JOptionPane.showMessageDialog(
                owner,
                scrollPane,
                titulo,
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}