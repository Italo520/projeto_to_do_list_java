// Em: src/main/java/br/com/todolist/ui/telaPrincipal/PainelEventos.java
package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Evento;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.DialogoEvento;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PainelEventos extends PainelBase {

    private final Orquestrador orquestrador;
    private DefaultListModel<Evento> modeloListaEventos;
    private JList<Evento> listaDeEventos;
    private JTextArea areaDescricao;

    public PainelEventos(Orquestrador orquestrador) {
        this.orquestrador = orquestrador;
        inicializarLayout();
    }

    // --- MÉTODOS PÚBLICOS PARA CONTROLE EXTERNO ---

    /**
     * MÉTODO CENTRALIZADO: Limpa a lista e exibe um novo conjunto de eventos.
     * É este o método que estava faltando e que a TelaPrincipal precisa chamar.
     * @param eventos A lista de eventos a ser exibida.
     */
    public void exibirEventos(List<Evento> eventos) {
        modeloListaEventos.clear();
        if (eventos != null) {
            eventos.forEach(modeloListaEventos::addElement);
        }
        areaDescricao.setText(""); // Limpa a área de descrição
    }

    /**
     * Carrega e exibe todos os eventos do usuário na lista.
     */
    public void exibirTodosOsEventos() {
        List<Evento> todosOsEventos = this.orquestrador.listarTodosEventos();
        exibirEventos(todosOsEventos);
    }


    // --- CONFIGURAÇÃO DA UI ---

    @Override
    protected JPanel criarPainelDeConteudo() {
        modeloListaEventos = new DefaultListModel<>();
        listaDeEventos = new JList<>(modeloListaEventos);
        listaDeEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaDeEventos.setBorder(BorderFactory.createTitledBorder("Eventos"));

        areaDescricao = new JTextArea();
        areaDescricao.setEditable(false);
        areaDescricao.setWrapStyleWord(true);
        areaDescricao.setLineWrap(true);
        areaDescricao.setBorder(BorderFactory.createTitledBorder("Descrição"));

        listaDeEventos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Evento eventoSelecionado = listaDeEventos.getSelectedValue();
                areaDescricao.setText(eventoSelecionado != null ? eventoSelecionado.getDescricao() : "");
            }
        });

        // Carrega os eventos iniciais ao criar o painel.
        exibirTodosOsEventos();

        JSplitPane painelDividido = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaDeEventos),
                new JScrollPane(areaDescricao));
        painelDividido.setDividerLocation(300);

        JPanel painelDeConteudo = new JPanel(new BorderLayout());
        painelDeConteudo.add(painelDividido, BorderLayout.CENTER);

        return painelDeConteudo;
    }
    
    @Override
    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton botaoNovoEvento = new JButton("Novo Evento");
        JButton botaoEditarEvento = new JButton("Editar Evento");
        JButton botaoExcluirEvento = new JButton("Excluir Evento");

        botaoNovoEvento.addActionListener(e -> adicionarNovoEvento());
        botaoEditarEvento.addActionListener(e -> editarEventoSelecionado());
        botaoExcluirEvento.addActionListener(e -> excluirEventoSelecionado());

        painel.add(botaoNovoEvento);
        painel.add(botaoEditarEvento);
        painel.add(botaoExcluirEvento);

        return painel;
    }

    private void adicionarNovoEvento() {
        Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogoEvento dialogo = new DialogoEvento(framePrincipal, orquestrador);
        dialogo.setVisible(true);

        if (dialogo.foiSalvo()) {
            exibirTodosOsEventos();
        }
    }

    private void editarEventoSelecionado() {
        Evento eventoSelecionado = listaDeEventos.getSelectedValue();
        if (eventoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogoEvento dialogo = new DialogoEvento(framePrincipal, orquestrador, eventoSelecionado);
        dialogo.setVisible(true);

        if (dialogo.foiSalvo()) {
            exibirTodosOsEventos();
        }
    }

    private void excluirEventoSelecionado() {
        Evento eventoSelecionado = listaDeEventos.getSelectedValue();
        if (eventoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o evento '" + eventoSelecionado.getTitulo() + "'?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            this.orquestrador.excluirEvento(eventoSelecionado);
            exibirTodosOsEventos();
        }
    }
}