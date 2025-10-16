package br.com.todolist.ui.telaPrincipal;


import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import br.com.todolist.controller.GerenteDeEventos;
import br.com.todolist.controller.GerenteDeTarefas;
import br.com.todolist.controller.Orquestrador;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.Repositorio;
import br.com.todolist.service.GeradorDeRelatorio;
import br.com.todolist.service.Mensageiro;

public class TelaPrincipal extends JFrame {

    private Orquestrador orquestrador;
    private PainelTarefas painelTarefas;
    private PainelEventos painelEventos;
    private JTabbedPane painelComAbas;

    public TelaPrincipal(Usuario usuarioLogado) {
        super("Usuário: " + usuarioLogado.getNome());

        this.orquestrador = new Orquestrador(new br.com.todolist.repository.RepositorioDB(), usuarioLogado);

        configurarJanela();
        montarLayout();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
    }

    private void montarLayout() {
        setJMenuBar(BarraFerramentas.criarBarraFerramentas(this, this.orquestrador));

        criarPaineis();
        painelComAbas.setBounds(5, 5, 1270, 650);

        add(painelComAbas);
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

    public void atualizarPainelDeEventos(List<Evento> eventos) {
        painelComAbas.setSelectedComponent(painelEventos);
        painelEventos.exibirEventos(eventos);
    }
}