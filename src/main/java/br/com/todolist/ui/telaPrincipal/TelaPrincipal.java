// Em: src/main/java/br/com/todolist/ui/telaPrincipal/TelaPrincipal.java
package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;
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

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void montarLayout() {
        // CORREÇÃO: Passando o orquestrador para a BarraFerramentas, como ela espera.
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

    // --- MÉTODOS PÚBLICOS PARA SEREM CHAMADOS PELA BARRA DE FERRAMENTAS ---

    /**
     * PADRONIZADO: Busca tarefas para um dia específico e comanda o painel para exibi-las.
     * Corresponde ao item de menu "Listar Tarefas por Dia".
     */
    public void listarTarefasPorDia(LocalDate dia) {
        List<Tarefa> tarefasFiltradas = orquestrador.listarTarefasPorDia(dia);
        painelComAbas.setSelectedComponent(painelTarefas); // Garante que a aba de tarefas esteja visível

        // Chamando o método centralizado do painel
        painelTarefas.exibirTarefas(tarefasFiltradas);
        
        if (tarefasFiltradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma tarefa encontrada para o dia selecionado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * PADRONIZADO: Comanda o PainelTarefas a remover os filtros e mostrar todos os itens.
     * Corresponde ao item de menu "Mostrar Todas as Tarefas".
     */
    public void mostrarTodasAsTarefas() {
        painelComAbas.setSelectedComponent(painelTarefas); // Garante que a aba de tarefas esteja visível
        
        // Chamando o método do painel para recarregar tudo
        painelTarefas.exibirTodasAsTarefas();
    }
}