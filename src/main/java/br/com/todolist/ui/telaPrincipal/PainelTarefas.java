package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Subtarefa;
import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.DialogoTarefa;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelTarefas extends PainelBase {

    private final Orquestrador orquestrador;

    private DefaultListModel<Tarefa> modeloListaTarefas;
    private DefaultListModel<Subtarefa> modeloListaSubtarefas;
    private JList<Tarefa> listaDeTarefas;
    private JList<Subtarefa> listaDeSubtarefas;

    private JLabel valorDescricao;
    private JLabel valorPrioridade;
    private JLabel valorPrazo;
    private JLabel valorConclusao;

    private final DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelTarefas(Orquestrador orquestrador) {
        super();
        this.orquestrador = orquestrador;
        super.inicializarLayout();
    }

    @Override
    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton botaoNovaTarefa = new JButton("Nova Tarefa");
        botaoNovaTarefa.addActionListener(new OuvinteBotaoNovaTarefa());

        JButton botaoEditarTarefa = new JButton("Editar Tarefa");
        botaoEditarTarefa.addActionListener(new OuvinteBotaoEditarTarefa());

        JButton botaoExcluirTarefa = new JButton("Excluir Tarefa");
        botaoExcluirTarefa.addActionListener(new OuvinteBotaoExcluirTarefa());

        painel.add(botaoNovaTarefa);
        painel.add(botaoEditarTarefa);
        painel.add(botaoExcluirTarefa);

        return painel;
    }

    @Override
    protected JPanel criarPainelDeConteudo() {
        modeloListaTarefas = new DefaultListModel<>();
        listaDeTarefas = new JList<>(modeloListaTarefas);
        listaDeTarefas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaDeTarefas.addListSelectionListener(new OuvinteSelecaoTarefa());
        JScrollPane scrollTarefas = new JScrollPane(listaDeTarefas);
        scrollTarefas.setBorder(BorderFactory.createTitledBorder("Tarefas"));

        JPanel painelDireito = criarPainelDireito();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTarefas, painelDireito);
        splitPane.setResizeWeight(0.5);

        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.add(splitPane, BorderLayout.CENTER);

        popularListaTarefas();
        return painelConteudo;
    }

    private JPanel criarPainelDireito() {
        JPanel painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(BorderFactory.createTitledBorder("Subtarefas e Detalhes"));

        painelDireito.add(criarPainelDetalhes(), BorderLayout.NORTH);
        painelDireito.add(criarPainelSubtarefas(), BorderLayout.CENTER);

        return painelDireito;
    }

    private JPanel criarPainelDetalhes() {
        JPanel painelDetalhes = new JPanel(new GridLayout(0, 2, 5, 5));
        painelDetalhes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        valorDescricao = new JLabel("N/D");
        valorPrioridade = new JLabel("N/D");
        valorPrazo = new JLabel("N/D");
        valorConclusao = new JLabel("N/D");

        painelDetalhes.add(new JLabel("Descrição:"));
        painelDetalhes.add(valorDescricao);
        painelDetalhes.add(new JLabel("Prioridade:"));
        painelDetalhes.add(valorPrioridade);
        painelDetalhes.add(new JLabel("Prazo:"));
        painelDetalhes.add(valorPrazo);
        painelDetalhes.add(new JLabel("Conclusão:"));
        painelDetalhes.add(valorConclusao);

        return painelDetalhes;
    }

    private JPanel criarPainelSubtarefas() {
        JPanel painelSubtarefas = new JPanel(new BorderLayout());
        modeloListaSubtarefas = new DefaultListModel<>();
        listaDeSubtarefas = new JList<>(modeloListaSubtarefas);
        listaDeSubtarefas.setCellRenderer(new SubtarefaCellRenderer());
        listaDeSubtarefas.addMouseListener(new OuvinteCliqueSubtarefa());

        JPanel painelBotoesSubtarefa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoNovaSubtarefa = new JButton("Nova Subtarefa");
        botaoNovaSubtarefa.addActionListener(new OuvinteBotaoNovaSubtarefa());
        JButton botaoEditarSubtarefa = new JButton("Editar Subtarefa");
        botaoEditarSubtarefa.addActionListener(new OuvinteBotaoEditarSubtarefa());
        JButton botaoExcluirSubtarefa = new JButton("Excluir Subtarefa");
        botaoExcluirSubtarefa.addActionListener(new OuvinteBotaoExcluirSubtarefa());

        painelBotoesSubtarefa.add(botaoNovaSubtarefa);
        painelBotoesSubtarefa.add(botaoEditarSubtarefa);
        painelBotoesSubtarefa.add(botaoExcluirSubtarefa);

        painelSubtarefas.add(new JScrollPane(listaDeSubtarefas), BorderLayout.CENTER);
        painelSubtarefas.add(painelBotoesSubtarefa, BorderLayout.SOUTH);

        return painelSubtarefas;
    }

    private void atualizarDetalhesTarefa(Tarefa tarefa) {
        if (tarefa != null) {
            valorDescricao.setText(tarefa.getDescricao());
            valorPrioridade.setText(String.valueOf(tarefa.getPrioridade()));
            valorPrazo.setText(tarefa.getDeadline().format(formatadorDeData));
            valorConclusao.setText((int) tarefa.obterPercentual() + "%");
        } else {
            valorDescricao.setText("Selecione uma tarefa");
            valorPrioridade.setText("-");
            valorPrazo.setText("-");
            valorConclusao.setText("-");
        }
    }

    private void popularListaTarefas() {
        modeloListaTarefas.clear();
        List<Tarefa> tarefas = orquestrador.listarTodasTarefas();
        if (tarefas != null) {
            tarefas.forEach(modeloListaTarefas::addElement);
        }
        atualizarListaSubtarefas(null);
        atualizarDetalhesTarefa(null);
    }

    private void atualizarListaSubtarefas(Tarefa tarefa) {
        modeloListaSubtarefas.clear();
        if (tarefa != null && tarefa.getSubtarefas() != null) {
            tarefa.getSubtarefas().forEach(modeloListaSubtarefas::addElement);
        }
    }
    
    public void exibirTarefasDoDia(List<Tarefa> tarefasDoDia) {
        modeloListaTarefas.clear();
        if (tarefasDoDia != null) {
            tarefasDoDia.forEach(modeloListaTarefas::addElement);
        }
        atualizarListaSubtarefas(null);
        atualizarDetalhesTarefa(null);
    }
    
    // OUVINTES

    private class OuvinteBotaoNovaTarefa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            DialogoTarefa dialogo = new DialogoTarefa((Frame) SwingUtilities.getWindowAncestor(PainelTarefas.this), orquestrador);
            dialogo.setVisible(true);
            if (dialogo.foiSalvo()) {
                popularListaTarefas();
            }
        }
    }

    private class OuvinteBotaoEditarTarefa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
            if (tarefaSelecionada == null) {
                JOptionPane.showMessageDialog(PainelTarefas.this, "Por favor, selecione uma tarefa para editar.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            DialogoTarefa dialogo = new DialogoTarefa((Frame) SwingUtilities.getWindowAncestor(PainelTarefas.this), orquestrador, tarefaSelecionada);
            dialogo.setVisible(true);
            if (dialogo.foiSalvo()) {
                popularListaTarefas();
            }
        }
    }

    private class OuvinteBotaoExcluirTarefa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
            if (tarefaSelecionada == null) {
                JOptionPane.showMessageDialog(PainelTarefas.this, "Por favor, selecione uma tarefa para excluir.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int resposta = JOptionPane.showConfirmDialog(PainelTarefas.this,
                    "Tem certeza que deseja excluir a tarefa:\n" + tarefaSelecionada.getTitulo(),
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (resposta == JOptionPane.YES_OPTION) {
                orquestrador.excluirTarefa(tarefaSelecionada);
                popularListaTarefas();
            }
        }
    }
    
    private class OuvinteSelecaoTarefa implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
                atualizarListaSubtarefas(tarefaSelecionada);
                atualizarDetalhesTarefa(tarefaSelecionada);
            }
        }
    }

    private class OuvinteCliqueSubtarefa extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int index = listaDeSubtarefas.locationToIndex(e.getPoint());
            if (index != -1) {
                Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
                Subtarefa subtarefa = modeloListaSubtarefas.getElementAt(index);
                subtarefa.mudarStatus();
                if (tarefaPai != null) {
                    orquestrador.atualizarTarefa(tarefaPai);
                    atualizarDetalhesTarefa(tarefaPai);
                }
                listaDeSubtarefas.repaint(listaDeSubtarefas.getCellBounds(index, index));
                listaDeTarefas.repaint();
            }
        }
    }
    
    private class OuvinteBotaoNovaSubtarefa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
            if (tarefaPai == null) {
                JOptionPane.showMessageDialog(PainelTarefas.this, "Selecione uma tarefa principal para adicionar uma subtarefa.", "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String descricao = JOptionPane.showInputDialog(PainelTarefas.this, "Descrição da nova subtarefa:", "Nova Subtarefa", JOptionPane.PLAIN_MESSAGE);
            if (descricao != null && !descricao.trim().isEmpty()) {
                tarefaPai.adicionarSubtarefa(new Subtarefa(descricao));
                orquestrador.atualizarTarefa(tarefaPai);
                atualizarListaSubtarefas(tarefaPai);
                atualizarDetalhesTarefa(tarefaPai);
                listaDeTarefas.repaint();
            }
        }
    }
    
    private class OuvinteBotaoEditarSubtarefa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
            Subtarefa subtarefa = listaDeSubtarefas.getSelectedValue();
            if (tarefaPai == null || subtarefa == null) {
                JOptionPane.showMessageDialog(PainelTarefas.this, "Selecione uma subtarefa para editar.", "Nenhuma Subtarefa Selecionada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String novoTitulo = (String) JOptionPane.showInputDialog(PainelTarefas.this, "Nova descrição:", "Editar Subtarefa", JOptionPane.PLAIN_MESSAGE, null, null, subtarefa.getTitulo());
            if (novoTitulo != null && !novoTitulo.trim().isEmpty()) {
                subtarefa.setTitulo(novoTitulo);
                orquestrador.atualizarTarefa(tarefaPai);
                atualizarListaSubtarefas(tarefaPai);
            }
        }
    }

    private class OuvinteBotaoExcluirSubtarefa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
            Subtarefa subtarefa = listaDeSubtarefas.getSelectedValue();
            if (tarefaPai == null || subtarefa == null) {
                JOptionPane.showMessageDialog(PainelTarefas.this, "Selecione uma subtarefa para excluir.", "Nenhuma Subtarefa Selecionada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int resposta = JOptionPane.showConfirmDialog(PainelTarefas.this, "Excluir a subtarefa '" + subtarefa.getTitulo() + "'?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                tarefaPai.removerSubtarefa(subtarefa);
                orquestrador.atualizarTarefa(tarefaPai);
                atualizarListaSubtarefas(tarefaPai);
                atualizarDetalhesTarefa(tarefaPai);
                listaDeTarefas.repaint();
            }
        }
    }
}