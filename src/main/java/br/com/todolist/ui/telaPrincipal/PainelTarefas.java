// Conteúdo final para: src/main/java/br/com/todolist/ui/telaPrincipal/PainelTarefas.java
package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Subtarefa;
import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.DialogoTarefa;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelTarefas extends PainelBase {

    private final Orquestrador orquestrador;

    private DefaultListModel<Tarefa> modeloListaTarefas;
    private DefaultListModel<Subtarefa> modeloListaSubtarefas;
    private JList<Tarefa> listaDeTarefas;
    private JList<Subtarefa> listaDeSubtarefas;

    public PainelTarefas(Orquestrador orquestrador) {
        super();
        this.orquestrador = orquestrador;
        // O método 'construir()' é chamado pelo construtor de PainelBase
    }

    @Override
    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

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
        // --- Painel Esquerdo: Lista de Tarefas ---
        modeloListaTarefas = new DefaultListModel<>();
        listaDeTarefas = new JList<>(modeloListaTarefas);
        listaDeTarefas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Adiciona um listener para atualizar as subtarefas quando uma tarefa é selecionada
        listaDeTarefas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                atualizarListaSubtarefas(listaDeTarefas.getSelectedValue());
            }
        });
        JScrollPane scrollTarefas = new JScrollPane(listaDeTarefas);
        scrollTarefas.setBorder(BorderFactory.createTitledBorder("Tarefas"));

        // --- Painel Direito: Lista de Subtarefas e Botões ---
        JPanel painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(BorderFactory.createTitledBorder("Subtarefas"));

        modeloListaSubtarefas = new DefaultListModel<>();
        listaDeSubtarefas = new JList<>(modeloListaSubtarefas);
        JScrollPane scrollSubtarefas = new JScrollPane(listaDeSubtarefas);
        
        // Botões para Subtarefas
        JPanel painelBotoesSubtarefa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoNovaSubtarefa = new JButton("Nova");
        JButton botaoEditarSubtarefa = new JButton("Editar");
        JButton botaoExcluirSubtarefa = new JButton("Excluir");
        
        // Adicionando ações aos botões de subtarefa
        botaoNovaSubtarefa.addActionListener(e -> adicionarNovaSubtarefa());
        botaoEditarSubtarefa.addActionListener(e -> editarSubtarefaSelecionada());
        botaoExcluirSubtarefa.addActionListener(e -> excluirSubtarefaSelecionada());

        painelBotoesSubtarefa.add(botaoNovaSubtarefa);
        painelBotoesSubtarefa.add(botaoEditarSubtarefa);
        painelBotoesSubtarefa.add(botaoExcluirSubtarefa);
        
        painelDireito.add(scrollSubtarefas, BorderLayout.CENTER);
        painelDireito.add(painelBotoesSubtarefa, BorderLayout.SOUTH);

        // --- Split Pane para dividir Tarefas e Subtarefas ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTarefas, painelDireito);
        splitPane.setResizeWeight(0.5); // Divide o espaço igualmente

        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.add(splitPane, BorderLayout.CENTER);

        // Carrega os dados iniciais do Orquestrador
        popularListaTarefas();

        return painelConteudo;
    }

    // --- MÉTODOS DE AÇÃO PARA TAREFAS ---

    private void popularListaTarefas() {
        modeloListaTarefas.clear();
        List<Tarefa> tarefas = orquestrador.listarTodasTarefas();
        if (tarefas != null) {
            tarefas.forEach(modeloListaTarefas::addElement);
        }
        // Limpa as subtarefas, pois a seleção de tarefa foi perdida
        atualizarListaSubtarefas(null);
    }

    private void adicionarNovaTarefa() {
        DialogoTarefa dialogo = new DialogoTarefa((Frame) SwingUtilities.getWindowAncestor(this), orquestrador);
        dialogo.setVisible(true);

        if (dialogo.foiSalvo()) {
            popularListaTarefas(); // Atualiza a lista se uma nova tarefa foi salva
        }
    }

    private void editarTarefaSelecionada() {
        Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
        if (tarefaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma tarefa para editar.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DialogoTarefa dialogo = new DialogoTarefa((Frame) SwingUtilities.getWindowAncestor(this), orquestrador, tarefaSelecionada);
        dialogo.setVisible(true);

        if (dialogo.foiSalvo()) {
            popularListaTarefas(); // Atualiza a lista se a tarefa foi editada
        }
    }

    private void excluirTarefaSelecionada() {
        Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
        if (tarefaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma tarefa para excluir.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a tarefa:\n" + tarefaSelecionada.getTitulo(),
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            orquestrador.excluirTarefa(tarefaSelecionada);
            popularListaTarefas(); // Atualiza a lista após a exclusão
        }
    }
    
    // --- MÉTODOS DE AÇÃO PARA SUBTAREFAS ---

    private void atualizarListaSubtarefas(Tarefa tarefa) {
        modeloListaSubtarefas.clear();
        if (tarefa != null && tarefa.getSubtarefas() != null) {
            tarefa.getSubtarefas().forEach(modeloListaSubtarefas::addElement);
        }
    }

    private void adicionarNovaSubtarefa() {
        Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
        if (tarefaPai == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa principal para adicionar uma subtarefa.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String descricao = JOptionPane.showInputDialog(this, "Descrição da nova subtarefa:", "Nova Subtarefa", JOptionPane.PLAIN_MESSAGE);
        if (descricao != null && !descricao.trim().isEmpty()) {
            tarefaPai.adicionarSubtarefa(new Subtarefa(descricao));
            
            // CHAMA O ORQUESTRADOR PARA PERSISTIR A MUDANÇA
            orquestrador.atualizarTarefa(tarefaPai);
            
            atualizarListaSubtarefas(tarefaPai);
            listaDeTarefas.repaint();
        }
    }

    private void editarSubtarefaSelecionada() {
        Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
        Subtarefa subtarefa = listaDeSubtarefas.getSelectedValue();
        
        if (tarefaPai == null || subtarefa == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma subtarefa para editar.", "Nenhuma Subtarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String novoTitulo = (String) JOptionPane.showInputDialog(this, "Nova descrição:", "Editar Subtarefa", JOptionPane.PLAIN_MESSAGE, null, null, subtarefa.getTitulo());
        if (novoTitulo != null && !novoTitulo.trim().isEmpty()) {
            subtarefa.setTitulo(novoTitulo);
            
            // CHAMA O ORQUESTRADOR PARA PERSISTIR A MUDANÇA
            orquestrador.atualizarTarefa(tarefaPai);

            atualizarListaSubtarefas(tarefaPai);
        }
    }

    private void excluirSubtarefaSelecionada() {
        Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
        Subtarefa subtarefa = listaDeSubtarefas.getSelectedValue();

        if (tarefaPai == null || subtarefa == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma subtarefa para excluir.", "Nenhuma Subtarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this, "Excluir a subtarefa '" + subtarefa.getTitulo() + "'?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            tarefaPai.removerSubtarefa(subtarefa);
            
            // CHAMA O ORQUESTRADOR PARA PERSISTIR A MUDANÇA
            orquestrador.atualizarTarefa(tarefaPai);
            
            atualizarListaSubtarefas(tarefaPai);
            listaDeTarefas.repaint();
        }
    }
}