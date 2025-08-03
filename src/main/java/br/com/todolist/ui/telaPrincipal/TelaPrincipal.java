// Em: src/main/java/br/com/todolist/ui/telaPrincipal/TelaPrincipal.java
package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import br.com.todolist.models.Usuario;
import br.com.todolist.service.Orquestrador; // Importe o Orquestrador

public class TelaPrincipal extends JFrame {

    private Orquestrador orquestrador;

    public TelaPrincipal(Usuario usuarioLogado) {
        super("ToDoLIst - Usuário: " + usuarioLogado.getNome());

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

        setJMenuBar(BarraFerramentas.criarBarraFerramentas(this));
        JTabbedPane painelComAbas = criarPaineis();

        setLayout(new BorderLayout());
        add(painelComAbas, BorderLayout.CENTER);
    }

    private JTabbedPane criarPaineis() {
        JTabbedPane aba = new JTabbedPane();

        // No futuro, os painéis também podem precisar do orquestrador
        // Ex: new PainelTarefas(this.orquestrador)
        aba.addTab("Tarefas", null, new PainelTarefas(this.orquestrador), "Gerenciador de Tarefas");
        aba.addTab("Eventos", null, new PainelEventos(this.orquestrador), "Gerenciador de Eventos");

        return aba;
    }

}