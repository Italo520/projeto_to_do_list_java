// Em: src/main/java/br/com/todolist/ui/telaPrincipal/TelaPrincipal.java
package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import br.com.todolist.models.Usuario;
import br.com.todolist.service.Orquestrador; // Importe o Orquestrador

public class TelaPrincipal extends JFrame {

    private Orquestrador orquestrador;

    // O construtor agora recebe o usuário que foi autenticado na tela de login
    public TelaPrincipal(Usuario usuarioLogado) {
        super("ToDoLIst - Usuário: " + usuarioLogado.getNome());

        // O Orquestrador é criado com base no usuário que fez o login
        this.orquestrador = new Orquestrador(usuarioLogado);

        configurarJanela();
        montarLayout();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void montarLayout() {
        // Precisamos passar 'this' para o ActionListener do relatório funcionar
        setJMenuBar(BarraFerramentas.criarBarraFerramentas(this));
        JTabbedPane painelComAbas = criarPaineis();

        setLayout(new BorderLayout());
        add(painelComAbas, BorderLayout.CENTER);
    }

    private JTabbedPane criarPaineis() {
        JTabbedPane aba = new JTabbedPane();

        // No futuro, os painéis também podem precisar do orquestrador
        // Ex: new PainelTarefas(this.orquestrador)
        aba.addTab("Tarefas", null, new PainelTarefas(), "Gerenciador de Tarefas");
        aba.addTab("Eventos", null, new PainelEventos(), "Gerenciador de Eventos");

        return aba;
    }

    // Método para ser chamado pelo menu da barra de ferramentas
    public void gerarRelatorioGeral() {
        String nomeDoArquivo = orquestrador.gerarRelatorioGeral();
        // ... Lógica para mostrar um JOptionPane, se desejar.
        System.out.println("Relatório " + nomeDoArquivo + " gerado!");
    }
}