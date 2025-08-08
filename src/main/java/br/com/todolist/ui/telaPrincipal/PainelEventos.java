package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Evento;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.DialogoEvento;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PainelEventos extends PainelBase {

    private final Orquestrador orquestrador;

    private DefaultListModel<Evento> modeloListaEventos;
    private JList<Evento> listaDeEventos;

    private JLabel valorDescricao;
    private JLabel valorTempoRestante;

    public PainelEventos(Orquestrador orquestrador) {
        this.orquestrador = orquestrador;
        inicializarLayout();
    }

    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoNovoEvento = new JButton("Novo Evento");
        JButton botaoEditarEvento = new JButton("Editar Evento");
        JButton botaoExcluirEvento = new JButton("Excluir Evento");

        botaoNovoEvento.addActionListener(new OuvinteBotaoNovoEvento());
        botaoEditarEvento.addActionListener(new OuvinteBotaoEditarEvento());
        botaoExcluirEvento.addActionListener(new OuvinteBotaoExcluirEvento());

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
        listaDeEventos.addListSelectionListener(new OuvinteSelecaoEvento());

        JPanel painelDetalhes = new JPanel(new BorderLayout());
        painelDetalhes.setBorder(BorderFactory.createTitledBorder("Detalhes do Evento"));

        JPanel painelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        valorDescricao = new JLabel();
        valorTempoRestante = new JLabel();

        painelCampos.add(new JLabel("Descrição:"));
        painelCampos.add(valorDescricao);
        painelCampos.add(new JLabel("Tempo Restante:"));
        painelCampos.add(valorTempoRestante);

        painelDetalhes.add(painelCampos, BorderLayout.NORTH);

        popularListaEventos();

        JSplitPane painelDividido = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaDeEventos),
                painelDetalhes);
        painelDividido.setDividerLocation(300);

        JPanel painelDeConteudo = new JPanel(new BorderLayout());
        painelDeConteudo.add(painelDividido, BorderLayout.CENTER);

        return painelDeConteudo;
    }


    private void atualizarDetalhesEvento(Evento evento) {
        if (evento != null) {
            valorDescricao.setText("<html>" + evento.getDescricao() + "</html>");

            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), evento.getDeadline());
            String textoTempo;
            if (diasRestantes > 1) {
                textoTempo = "Faltam " + diasRestantes + " dias";
            } else if (diasRestantes == 1) {
                textoTempo = "Falta 1 dia";
            } else if (diasRestantes == 0) {
                textoTempo = "É hoje!";
            } else {
                textoTempo = "Atrasado";
            }
            valorTempoRestante.setText(textoTempo);
        } else {
            valorDescricao.setText("Selecione um evento");
            valorTempoRestante.setText("-");
        }
    }

    private void popularListaEventos() {
        modeloListaEventos.clear();
        for (Evento evento : this.orquestrador.listarTodosEventos()) {
            modeloListaEventos.addElement(evento);
        }
        atualizarDetalhesEvento(null);
    }

    public void exibirEventos(List<Evento> eventos) {
        modeloListaEventos.clear();
        if (eventos != null) {
            eventos.forEach(modeloListaEventos::addElement);
        }
        atualizarDetalhesEvento(null);
    }
    
    // OUVINTES
    
    private class OuvinteBotaoNovoEvento implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(PainelEventos.this);
            DialogoEvento dialogo = new DialogoEvento(framePrincipal, orquestrador);
            dialogo.setVisible(true);

            if (dialogo.foiSalvo()) {
                popularListaEventos();
            }
        }
    }

    private class OuvinteBotaoEditarEvento implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Evento eventoSelecionado = listaDeEventos.getSelectedValue();
            if (eventoSelecionado == null) {
                JOptionPane.showMessageDialog(PainelEventos.this, "Selecione um evento para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(PainelEventos.this);
            DialogoEvento dialogo = new DialogoEvento(framePrincipal, orquestrador, eventoSelecionado);
            dialogo.setVisible(true);

            if (dialogo.foiSalvo()) {
                popularListaEventos();
            }
        }
    }

    private class OuvinteBotaoExcluirEvento implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Evento eventoSelecionado = listaDeEventos.getSelectedValue();
            if (eventoSelecionado == null) {
                JOptionPane.showMessageDialog(PainelEventos.this, "Selecione um evento para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(PainelEventos.this,
                    "Tem certeza que deseja excluir o evento '" + eventoSelecionado.getTitulo() + "'?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                orquestrador.excluirEvento(eventoSelecionado);
                popularListaEventos();
            }
        }
    }

    private class OuvinteSelecaoEvento implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                Evento eventoSelecionado = listaDeEventos.getSelectedValue();
                atualizarDetalhesEvento(eventoSelecionado);
            }
        }
    }
}