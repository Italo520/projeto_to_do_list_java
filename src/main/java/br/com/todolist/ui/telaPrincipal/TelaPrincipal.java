// Em: src/main/java/br/com/todolist/ui/telaPrincipal/TelaPrincipal.java
package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.YearMonth; // Importar YearMonth
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;
import br.com.todolist.models.Evento; // Importar Evento
import br.com.todolist.models.Tarefa;
import br.com.todolist.models.Usuario;
import br.com.todolist.service.Orquestrador;

public class TelaPrincipal extends JFrame {

    private Orquestrador orquestrador;
    private JTabbedPane painelComAbas;
    private PainelTarefas painelTarefas;
    private PainelEventos painelEventos;

    public TelaPrincipal(Usuario usuarioLogado) {
        super("ToDoLIst - Usuário: " + usuarioLogado.getNome());
        this.orquestrador = new Orquestrador(usuarioLogado);
        configurarJanela();
        montarLayout();
    }

    // --- MÉTODOS DE CONTROLE PARA EVENTOS ---

    /**
     * Filtra e exibe os eventos de um dia específico.
     */
    public void listarEventosPorDia(LocalDate dia) {
        List<Evento> eventosFiltrados = orquestrador.listarEventosPorDia(dia);
        painelComAbas.setSelectedComponent(painelEventos);
        painelEventos.exibirEventos(eventosFiltrados);

        if (eventosFiltrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum evento encontrado para este dia.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Filtra e exibe os eventos de um mês/ano específico.
     */
    public void listarEventosPorMes(YearMonth mes) {
        List<Evento> eventosFiltrados = orquestrador.listarEventosPorMes(mes);
        painelComAbas.setSelectedComponent(painelEventos);
        painelEventos.exibirEventos(eventosFiltrados);

        if (eventosFiltrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum evento encontrado para este mês.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Remove os filtros e mostra todos os eventos.
     */
    public void mostrarTodosOsEventos() {
        painelComAbas.setSelectedComponent(painelEventos);
        painelEventos.exibirTodosOsEventos();
    }
    
    // ... (O restante da classe TelaPrincipal permanece o mesmo)
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

    public void listarTarefasPorDia(LocalDate dia) {
        List<Tarefa> tarefasFiltradas = orquestrador.listarTarefasPorDia(dia);
        painelComAbas.setSelectedComponent(painelTarefas);
        painelTarefas.exibirTarefas(tarefasFiltradas);
        
        if (tarefasFiltradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma tarefa encontrada para o dia selecionado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void mostrarTodasAsTarefas() {
        painelComAbas.setSelectedComponent(painelTarefas);
        painelTarefas.exibirTodasAsTarefas();
    }
}