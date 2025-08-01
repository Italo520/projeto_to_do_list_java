// Substitua o conteúdo em: src/main/java/br/com/todolist/ui/telaPrincipal/PainelEventos.java

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
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import br.com.todolist.models.Evento;
import br.com.todolist.ui.TelasDialogo.DialogoEvento;

public class PainelEventos extends PainelBase {

    private DefaultListModel<Evento> modeloListaEventos;
    private JList<Evento> listaDeEventos;
    private JTextArea areaDescricao;

    private List<Evento> meusEventos;

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
                if (eventoSelecionado != null) {
                    areaDescricao.setText(eventoSelecionado.getDescricao());
                } else {
                    areaDescricao.setText("");
                }
            }
        });

        this.meusEventos = carregarEventosDeExemplo();
        popularListaEventos();

        JSplitPane painelDividido = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaDeEventos),
                new JScrollPane(areaDescricao));
        painelDividido.setDividerLocation(300);

        JPanel painelDeConteudo = new JPanel(new BorderLayout());
        painelDeConteudo.add(painelDividido, BorderLayout.CENTER);

        return painelDeConteudo;
    }

    private void popularListaEventos() {
        modeloListaEventos.clear();
        for (Evento evento : this.meusEventos) {
            modeloListaEventos.addElement(evento);
        }
    }

    private void adicionarNovoEvento() {
        Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogoEvento dialogo = new DialogoEvento(framePrincipal);
        dialogo.setVisible(true);

        if (dialogo.foiSalvo()) {
            Evento novoEvento = dialogo.getEvento();
            this.meusEventos.add(novoEvento);
            popularListaEventos();
            listaDeEventos.setSelectedValue(novoEvento, true);
        }
    }

    private void editarEventoSelecionado() {
        Evento eventoSelecionado = listaDeEventos.getSelectedValue();
        if (eventoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogoEvento dialogo = new DialogoEvento(framePrincipal, eventoSelecionado);
        dialogo.setVisible(true);

        if (dialogo.foiSalvo()) {
            listaDeEventos.repaint();
            areaDescricao.setText(eventoSelecionado.getDescricao());
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
            this.meusEventos.remove(eventoSelecionado);
            popularListaEventos();
        }
    }

    private List<Evento> carregarEventosDeExemplo() {
        List<Evento> eventos = new ArrayList<>();
        eventos.add(new Evento("Reunião de equipe", "Reunião semanal para alinhamento de sprints.", LocalDate.now(), LocalDate.now().plusDays(2)));
        eventos.add(new Evento("Aniversário do projeto", "Comemoração de 1 ano do projeto ToDoList.", LocalDate.now(), LocalDate.now().plusDays(15)));
        eventos.add(new Evento("Workshop de Swing", "Workshop avançado sobre componentes Swing e boas práticas.", LocalDate.now(), LocalDate.now().plusMonths(1)));
        return eventos;
    }
}