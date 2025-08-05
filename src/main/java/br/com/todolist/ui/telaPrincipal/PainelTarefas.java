// Em: src/main/java/br/com/todolist/ui/telaPrincipal/PainelTarefas.java
package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Subtarefa;
import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.DialogoTarefa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        super.inicializarLayout();
    }

    // --- MÉTODOS PÚBLICOS PARA CONTROLE EXTERNO ---

    /**
     * MÉTODO CENTRALIZADO: Limpa a lista atual e exibe uma nova lista de tarefas.
     * Este é o único método que a TelaPrincipal usará para atualizar a exibição.
     * @param tarefas A lista de tarefas (filtrada ou completa) a ser exibida.
     */
    public void exibirTarefas(List<Tarefa> tarefas) {
        modeloListaTarefas.clear();
        if (tarefas != null) {
            tarefas.forEach(modeloListaTarefas::addElement);
        }
        // Limpa a seleção e as subtarefas
        listaDeTarefas.clearSelection();
        atualizarListaSubtarefas(null);
    }

    /**
     * Carrega e exibe todas as tarefas do usuário.
     * Chamado quando o painel é criado ou quando o filtro é limpo.
     */
    public void exibirTodasAsTarefas() {
        List<Tarefa> todasAsTarefas = orquestrador.listarTodasTarefas();
        exibirTarefas(todasAsTarefas);
    }


    // --- MÉTODOS DE CONFIGURAÇÃO DA UI (INTERNOS) ---

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
        // ... (o restante do método criarPainelDeConteudo permanece o mesmo)
        modeloListaTarefas = new DefaultListModel<>();
        listaDeTarefas = new JList<>(modeloListaTarefas);
        listaDeTarefas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaDeTarefas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                atualizarListaSubtarefas(listaDeTarefas.getSelectedValue());
            }
        });
        JScrollPane scrollTarefas = new JScrollPane(listaDeTarefas);
        scrollTarefas.setBorder(BorderFactory.createTitledBorder("Tarefas"));

        JPanel painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(BorderFactory.createTitledBorder("Subtarefas"));
        modeloListaSubtarefas = new DefaultListModel<>();
        listaDeSubtarefas = new JList<>(modeloListaSubtarefas);
        listaDeSubtarefas.setCellRenderer(new SubtarefaCellRenderer());
        adicionarListenerDeCliqueSubtarefa();
        JScrollPane scrollSubtarefas = new JScrollPane(listaDeSubtarefas);

        JPanel painelBotoesSubtarefa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoNovaSubtarefa = new JButton("Nova");
        JButton botaoEditarSubtarefa = new JButton("Editar");
        JButton botaoExcluirSubtarefa = new JButton("Excluir");
        botaoNovaSubtarefa.addActionListener(e -> adicionarNovaSubtarefa());
        botaoEditarSubtarefa.addActionListener(e -> editarSubtarefaSelecionada());
        botaoExcluirSubtarefa.addActionListener(e -> excluirSubtarefaSelecionada());
        painelBotoesSubtarefa.add(botaoNovaSubtarefa);
        painelBotoesSubtarefa.add(botaoEditarSubtarefa);
        painelBotoesSubtarefa.add(botaoExcluirSubtarefa);

        painelDireito.add(scrollSubtarefas, BorderLayout.CENTER);
        painelDireito.add(painelBotoesSubtarefa, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTarefas, painelDireito);
        splitPane.setResizeWeight(0.5);

        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.add(splitPane, BorderLayout.CENTER);

        // Ao iniciar o painel, exibe todas as tarefas por padrão.
        exibirTodasAsTarefas();
        return painelConteudo;
    }
    
    // ... (o restante da classe com os métodos de ação dos botões permanece o mesmo)
    // ... (adicionarNovaTarefa, editarTarefaSelecionada, etc.)
    // ... (SubtarefaCellRenderer, etc.)

    private void adicionarListenerDeCliqueSubtarefa() {
        listaDeSubtarefas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int indexSubtarefa = listaDeSubtarefas.locationToIndex(e.getPoint());
                if (indexSubtarefa != -1) {
                    Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
                    Subtarefa subtarefa = modeloListaSubtarefas.getElementAt(indexSubtarefa);
                    
                    subtarefa.setStatus(!subtarefa.isStatus());
                    orquestrador.atualizarTarefa(tarefaPai);
                    
                    // ATUALIZAÇÃO FORÇADA: Recarrega toda a lista de tarefas para refletir o progresso
                    int selectedIndex = listaDeTarefas.getSelectedIndex();
                    exibirTodasAsTarefas();
                    listaDeTarefas.setSelectedIndex(selectedIndex); // Tenta manter a seleção
                    
                    listaDeSubtarefas.repaint();
                }
            }
        });
    }

    private void atualizarListaSubtarefas(Tarefa tarefa) {
        modeloListaSubtarefas.clear();
        if (tarefa != null && tarefa.getSubtarefas() != null) {
            tarefa.getSubtarefas().forEach(modeloListaSubtarefas::addElement);
        }
    }

    private void adicionarNovaTarefa() {
        DialogoTarefa dialogo = new DialogoTarefa((Frame) SwingUtilities.getWindowAncestor(this), orquestrador);
        dialogo.setVisible(true);
        if (dialogo.foiSalvo()) {
            exibirTodasAsTarefas();
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
            exibirTodasAsTarefas();
        }
    }

    private void excluirTarefaSelecionada() {
        Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
        if (tarefaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma tarefa para excluir.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir a tarefa:\n" + tarefaSelecionada.getTitulo(), "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resposta == JOptionPane.YES_OPTION) {
            orquestrador.excluirTarefa(tarefaSelecionada);
            exibirTodasAsTarefas();
        }
    }
    
    private void adicionarNovaSubtarefa() {
        Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
        if (tarefaPai == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa para adicionar uma subtarefa.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String descricao = JOptionPane.showInputDialog(this, "Descrição da nova subtarefa:", "Nova Subtarefa", JOptionPane.PLAIN_MESSAGE);
        if (descricao != null && !descricao.trim().isEmpty()) {
            tarefaPai.adicionarSubtarefa(new Subtarefa(descricao));
            orquestrador.atualizarTarefa(tarefaPai);
            exibirTodasAsTarefas();
        }
    }

    private void editarSubtarefaSelecionada() {
        Subtarefa subtarefa = listaDeSubtarefas.getSelectedValue();
        if (subtarefa == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma subtarefa para editar.", "Nenhuma Subtarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String novoTitulo = (String) JOptionPane.showInputDialog(this, "Nova descrição:", "Editar Subtarefa", JOptionPane.PLAIN_MESSAGE, null, null, subtarefa.getTitulo());
        if (novoTitulo != null && !novoTitulo.trim().isEmpty()) {
            subtarefa.setTitulo(novoTitulo);
            orquestrador.atualizarTarefa(listaDeTarefas.getSelectedValue());
            listaDeSubtarefas.repaint();
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
            orquestrador.atualizarTarefa(tarefaPai);
            exibirTodasAsTarefas();
        }
    }

    class SubtarefaCellRenderer extends JCheckBox implements ListCellRenderer<Subtarefa> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Subtarefa> list, Subtarefa value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.getTitulo());
            setSelected(value.isStatus());
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setFocusPainted(false);
            return this;
        }
    }

}