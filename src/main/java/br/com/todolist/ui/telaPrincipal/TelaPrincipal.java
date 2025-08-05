// Em: src/main/java/br/com/todolist/ui/telaPrincipal/TelaPrincipal.java
package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import br.com.todolist.models.Tarefa;
import br.com.todolist.models.Usuario;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.PadraoDialogo;

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

    // --- MÉTODOS PÚBLICOS PARA COMUNICAÇÃO (A "PONTE") ---

    /**
     * CORREÇÃO: Este método agora recebe a data, busca os dados e comanda o painel.
     */
    public void filtrarTarefasDaTelaPrincipal(LocalDate dia) {
        // 1. A TelaPrincipal chama o orquestrador para buscar os dados
        List<Tarefa> tarefasFiltradas = orquestrador.listarTarefasPorDia(dia);

        // 2. Garante que a aba de tarefas esteja visível
        painelComAbas.setSelectedComponent(painelTarefas);
        
        // 3. Comanda o PainelTarefas a se atualizar com a lista filtrada
        painelTarefas.exibirListaFiltrada(tarefasFiltradas);
        
        // 4. Mostra um aviso se nada for encontrado
        if (tarefasFiltradas.isEmpty()) {
            PadraoDialogo.mostrarMensagemInfo(this, "Nenhuma tarefa encontrada para o dia selecionado.");
        }
    }

    /**
     * Comanda o PainelTarefas a remover os filtros e mostrar todos os itens.
     */
    public void mostrarTodasAsTarefas() {
        painelComAbas.setSelectedComponent(painelTarefas);
        painelTarefas.popularComTodasAsTarefas();
    }
}