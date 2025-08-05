// Em: src/main/java/br/com/todolist/ui/telaPrincipal/TelaPrincipal.java
package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import java.util.List; // MUDANÇA: Import necessário
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import br.com.todolist.models.Tarefa; // MUDANÇA: Import necessário
import br.com.todolist.models.Usuario;
import br.com.todolist.service.Orquestrador;

public class TelaPrincipal extends JFrame {

    private Orquestrador orquestrador;
    // MUDANÇA: Transformando os painéis em campos da classe
    private PainelTarefas painelTarefas;
    private PainelEventos painelEventos;
    private JTabbedPane painelComAbas;

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
        setJMenuBar(BarraFerramentas.criarBarraFerramentas(this, this.orquestrador));
        
        criarPaineis();

        setLayout(new BorderLayout());
        add(painelComAbas, BorderLayout.CENTER);
    }

    private void criarPaineis() {
        painelComAbas = new JTabbedPane();
        
        this.painelTarefas = new PainelTarefas(this.orquestrador);
        this.painelEventos = new PainelEventos(this.orquestrador);

        painelComAbas.addTab("Tarefas", null, this.painelTarefas, "Gerenciador de Tarefas");
        painelComAbas.addTab("Eventos", null, this.painelEventos, "Gerenciador de Eventos");
    }

    public void atualizarPainelDeTarefas(List<Tarefa> tarefas) {

        painelComAbas.setSelectedComponent(painelTarefas);
        painelTarefas.exibirTarefasDoDia(tarefas);
    }
}