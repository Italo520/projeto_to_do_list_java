// Em: src/main/java/br/com/todolist/ui/telaPrincipal/PainelTarefas.java
package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Subtarefa;
import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.DialogoTarefa;
import br.com.todolist.ui.TelasDialogo.PadraoDialogo;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional; // MUDANÇA: Import que faltava

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

    // --- MÉTODOS PÚBLICOS PARA SEREM CONTROLADOS PELA TELA PRINCIPAL ---
    public void exibirListaFiltrada(List<Tarefa> tarefas) {
        modeloListaTarefas.clear();
        if (tarefas != null) {
            tarefas.forEach(modeloListaTarefas::addElement);
        }
        atualizarListaSubtarefas(null);
    }
    
    public void popularComTodasAsTarefas() {
        modeloListaTarefas.clear();
        List<Tarefa> tarefas = orquestrador.listarTodasTarefas();
        if (tarefas != null) {
            tarefas.forEach(modeloListaTarefas::addElement);
        }
        atualizarListaSubtarefas(null);
    }

    // --- O RESTO DA CLASSE COM AS CORREÇÕES NAS CHAMADAS DE DIÁLOGO ---
    
    // Método para obter a janela pai (JFrame) a partir do painel (JPanel)
    private JFrame getFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
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
        modeloListaTarefas = new DefaultListModel<>();
        listaDeTarefas = new JList<>(modeloListaTarefas);
        listaDeTarefas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaDeTarefas.setCellRenderer(new TarefaCellRenderer());
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
        popularComTodasAsTarefas();
        return painelConteudo;
    }

    private void adicionarNovaTarefa() {
        DialogoTarefa dialogo = new DialogoTarefa(getFrame(), orquestrador);
        dialogo.setVisible(true);
        if (dialogo.foiSalvo()) {
            popularComTodasAsTarefas();
        }
    }

    private void editarTarefaSelecionada() {
        Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
        if (tarefaSelecionada == null) {
            PadraoDialogo.mostrarMensagemErro(getFrame(), "Por favor, selecione uma tarefa para editar.");
            return;
        }
        DialogoTarefa dialogo = new DialogoTarefa(getFrame(), orquestrador, tarefaSelecionada);
        dialogo.setVisible(true);
        if (dialogo.foiSalvo()) {
            popularComTodasAsTarefas();
        }
    }

    private void excluirTarefaSelecionada() {
        Tarefa tarefaSelecionada = listaDeTarefas.getSelectedValue();
        if (tarefaSelecionada == null) {
            PadraoDialogo.mostrarMensagemErro(getFrame(), "Por favor, selecione uma tarefa para excluir.");
            return;
        }
        if (PadraoDialogo.confirmarAcao(getFrame(), "Confirmar Exclusão", "Tem certeza que deseja excluir a tarefa:\n" + tarefaSelecionada.getTitulo())) {
            orquestrador.excluirTarefa(tarefaSelecionada);
            popularComTodasAsTarefas();
        }
    }

    private class TarefaCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Tarefa) {
                Tarefa tarefa = (Tarefa) value;
                double percentual = orquestrador.calcularPercentualTarefa(tarefa);
                setText(tarefa.getTitulo() + " (Conclusão: " + (int) percentual + "%)");
            }
            return this;
        }
    }


    private void atualizarListaSubtarefas(Tarefa tarefa) {
        modeloListaSubtarefas.clear();
        if (tarefa != null && tarefa.getSubtarefas() != null) {
            tarefa.getSubtarefas().forEach(modeloListaSubtarefas::addElement);
        }
    }

    private void adicionarNovaSubtarefa() {
        Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
        if (tarefaPai == null) {
            PadraoDialogo.mostrarMensagemErro(getFrame(), "Selecione uma tarefa principal para adicionar uma subtarefa.");
            return;
        }
        Optional<String> descricao = PadraoDialogo.pedirTexto(getFrame(), "Nova Subtarefa", "Descrição da nova subtarefa:");
        descricao.ifPresent(desc -> {
            tarefaPai.adicionarSubtarefa(new Subtarefa(desc));
            orquestrador.atualizarTarefa(tarefaPai);
            atualizarListaSubtarefas(tarefaPai);
            listaDeTarefas.repaint();
        });
    }

    private void editarSubtarefaSelecionada() {
        Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
        Subtarefa subtarefa = listaDeSubtarefas.getSelectedValue();
        if (tarefaPai == null || subtarefa == null) {
            PadraoDialogo.mostrarMensagemErro(getFrame(), "Selecione uma subtarefa para editar.");
            return;
        }
        Optional<String> novoTitulo = PadraoDialogo.pedirTexto(getFrame(), "Editar Subtarefa", "Nova descrição:");
        novoTitulo.ifPresent(titulo -> {
             subtarefa.setTitulo(titulo);
             orquestrador.atualizarTarefa(tarefaPai);
             atualizarListaSubtarefas(tarefaPai);
        });
    }
    
    private void excluirSubtarefaSelecionada() {
        Tarefa tarefaPai = listaDeTarefas.getSelectedValue();
        Subtarefa subtarefa = listaDeSubtarefas.getSelectedValue();
        if (tarefaPai == null || subtarefa == null) {
            PadraoDialogo.mostrarMensagemErro(getFrame(), "Selecione uma subtarefa para excluir.");
            return;
        }
        if (PadraoDialogo.confirmarAcao(getFrame(), "Confirmar Exclusão", "Excluir a subtarefa '" + subtarefa.getTitulo() + "'?")) {
            tarefaPai.removerSubtarefa(subtarefa);
            orquestrador.atualizarTarefa(tarefaPai);
            atualizarListaSubtarefas(tarefaPai);
            listaDeTarefas.repaint();
        }
    }
}