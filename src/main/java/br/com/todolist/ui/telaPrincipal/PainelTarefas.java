// Em: src/main/java/br/com/todolist/ui/telaPrincipal/PainelTarefas.java

package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import br.com.todolist.models.Subtarefa;
import br.com.todolist.models.Tarefa;
import br.com.todolist.ui.TelasDialogo.DialogoTarefa;

public class PainelTarefas extends PainelBase {

    private DefaultListModel<Tarefa> modeloListaTarefas;
    private DefaultListModel<Subtarefa> modeloListaSubtarefas;

    private JList<Tarefa> listaDeTarefas;
    private JList<Subtarefa> listaDeSubtarefas;

    private List<Tarefa> minhasTarefas;

    public PainelTarefas() {
        super();
    }

    @Override
    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      

        JButton botaoNovaTarefa = new JButton("Nova Tarefa");
        JButton botaoEditarTarefa = new JButton("Editar Tarefa");
        JButton botaoExcluirTarefa = new JButton("Excluir Tarefa");

        botaoNovaTarefa.addActionListener(e -> adicionarNovaTarefa());
        botaoEditarTarefa.addActionListener(e -> editarTarefaSelecionada());
        botaoExcluirTarefa.addActionListener(e -> excluirTarefaSelecionada());
        
        painel.add(botaoNovaTarefa);
        painel.add(botaoEditarTarefa);
        painel.add(botaoExcluirTarefa);

        return painel;
    }

    @Override
    protected JPanel criarPainelDeConteudo() {
        modeloListaTarefas = new DefaultListModel<>();
        modeloListaSubtarefas = new DefaultListModel<>();

        listaDeTarefas = new JList<>(modeloListaTarefas);
        listaDeTarefas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaDeTarefas.setBorder(BorderFactory.createTitledBorder("Tarefas"));

        listaDeSubtarefas = new JList<>(modeloListaSubtarefas);

        listaDeTarefas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
                atualizarListaSubtarefas(tarefaSelecionada);
            }
        });

        // O método agora está implementado e será chamado corretamente
        this.minhasTarefas = carregarTarefasDeExemplo();
        popularListaTarefas();
        
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.setBorder(BorderFactory.createTitledBorder("Subtarefas"));
        
        JPanel painelBotoesSubtarefa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoNovaSubtarefa = new JButton("Nova Subtarefa");
        JButton botaoEditarSubtarefa = new JButton("Editar Subtarefa");
        JButton botaoExcluirSubtarefa = new JButton("Excluir Subtarefa");
        painelBotoesSubtarefa.add(botaoNovaSubtarefa);
        painelBotoesSubtarefa.add(botaoEditarSubtarefa);
        painelBotoesSubtarefa.add(botaoExcluirSubtarefa);

        botaoNovaSubtarefa.addActionListener(e -> adicionarNovaSubtarefa());
        botaoEditarSubtarefa.addActionListener(e -> editarSubtarefaSelecionada());
        botaoExcluirSubtarefa.addActionListener(e -> excluirSubtarefaSelecionada());

        painelDireito.add(new JScrollPane(listaDeSubtarefas), BorderLayout.CENTER);
        painelDireito.add(painelBotoesSubtarefa, BorderLayout.SOUTH);

        JSplitPane painelDividido = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaDeTarefas),
                painelDireito);
        painelDividido.setDividerLocation(300);

        JPanel painelDeConteudo = new JPanel(new BorderLayout());
        painelDeConteudo.add(painelDividido, BorderLayout.CENTER);

        return painelDeConteudo;
    }

    // --- MÉTODOS PARA MANIPULAR TAREFAS ---

    private void popularListaTarefas() {
        modeloListaTarefas.clear();
        for (Tarefa tarefa : this.minhasTarefas) {
            modeloListaTarefas.addElement(tarefa);
        }
    }

    private void adicionarNovaTarefa() {
        Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogoTarefa dialogo = new DialogoTarefa(framePrincipal);
        dialogo.setVisible(true);

        if (dialogo.foiSalvo()) {
            Tarefa novaTarefa = dialogo.getTarefa();
            this.minhasTarefas.add(novaTarefa);
            popularListaTarefas();
            listaDeTarefas.setSelectedValue(novaTarefa, true);
        }
    }

    private void editarTarefaSelecionada() {
        Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
        if (tarefaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma tarefa para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogoTarefa dialogo = new DialogoTarefa(framePrincipal, tarefaSelecionada);
        dialogo.setVisible(true);
        if (dialogo.foiSalvo()) {
            repaint();
        }
    }

    private void excluirTarefaSelecionada() {
        Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
        if (tarefaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma tarefa para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a tarefa '" + tarefaSelecionada.getTitulo() + "'?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            this.minhasTarefas.remove(tarefaSelecionada);
            popularListaTarefas();
        }
    }

    // --- MÉTODOS PARA MANIPULAR SUBTAREFAS ---

    private void atualizarListaSubtarefas(Tarefa tarefa) {
        modeloListaSubtarefas.clear();
        if (tarefa != null) {
            for (Subtarefa subtarefa : tarefa.getSubtarefas()) {
                modeloListaSubtarefas.addElement(subtarefa);
            }
        }
    }

    private void adicionarNovaSubtarefa() {
        Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
        if (tarefaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa principal primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeSubtarefa = JOptionPane.showInputDialog(this, "Nome da nova subtarefa:", "Nova Subtarefa", JOptionPane.PLAIN_MESSAGE);
        if (nomeSubtarefa != null && !nomeSubtarefa.trim().isEmpty()) {
            Subtarefa novaSubtarefa = new Subtarefa(nomeSubtarefa);
            tarefaSelecionada.adicionarSubtarefa(novaSubtarefa);
            atualizarListaSubtarefas(tarefaSelecionada);
        }
    }

    private void editarSubtarefaSelecionada() {
        Subtarefa subtarefaSelecionada = listaDeSubtarefas.getSelectedValue();
        if (subtarefaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma subtarefa para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String novoNome = (String) JOptionPane.showInputDialog(this,"Editar nome da subtarefa:","Editar Subtarefa",JOptionPane.QUESTION_MESSAGE,null,null,subtarefaSelecionada.getTitulo()
);
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            subtarefaSelecionada.setTitulo(novoNome);
            listaDeSubtarefas.repaint();
        }
    }

    private void excluirSubtarefaSelecionada() {
        Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
        Subtarefa subtarefaSelecionada = listaDeSubtarefas.getSelectedValue();

        if (tarefaPai == null || subtarefaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma subtarefa para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a subtarefa '" + subtarefaSelecionada.getTitulo() + "'?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            tarefaPai.removerSubtarefa(subtarefaSelecionada);
            atualizarListaSubtarefas(tarefaPai);
        }
    }
    
    /**
     * MÉTODO AGORA IMPLEMENTADO CORRETAMENTE.
     * Cria uma lista de tarefas de exemplo para popular a UI inicialmente.
     */
    private List<Tarefa> carregarTarefasDeExemplo() {
        List<Tarefa> tarefas = new ArrayList<>();
        
        Tarefa t1 = new Tarefa("Teste N° 1", "teste de tarefa", LocalDate.now().plusDays(10), 1);
        t1.adicionarSubtarefa(new Subtarefa("teste de funcionamento de subtarefas"));
        t1.adicionarSubtarefa(new Subtarefa("teste2 de funcionamento de subtarefas"));
        
        Tarefa t2 = new Tarefa("teste 2", "teste de Tarefas2", LocalDate.now().plusDays(5), 2);
        t2.adicionarSubtarefa(new Subtarefa("testando subtarefas1"));
        t2.adicionarSubtarefa(new Subtarefa("testando subtarefas2"));

        Tarefa t3 = new Tarefa("Teste 3", "Mais um teste de funcionamento", LocalDate.now().plusDays(7), 3);

        tarefas.add(t1);
        tarefas.add(t2);
        tarefas.add(t3);
        
        return tarefas;
    }
}