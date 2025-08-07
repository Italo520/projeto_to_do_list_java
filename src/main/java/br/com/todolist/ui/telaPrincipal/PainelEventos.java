package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Evento;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.DialogoEvento;

import javax.swing.*;
import java.awt.*;
// NOVO! Imports para lidar com datas.
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PainelEventos extends PainelBase {

    private final Orquestrador orquestrador;

    private DefaultListModel<Evento> modeloListaEventos;
    private JList<Evento> listaDeEventos;
    
    // REMOVIDO/SUBSTITUÍDO: A JTextArea não é mais necessária.
    // private JTextArea areaDescricao;
    
    // NOVO! Labels para os detalhes do evento.
    private JLabel valorDescricao;
    private JLabel valorTempoRestante;


    public PainelEventos(Orquestrador orquestrador) {
        this.orquestrador = orquestrador;
        inicializarLayout();
    }

    @Override
    protected JPanel criarPainelDeBotoes() {
        // (Este método não precisa de alterações)
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
        
        // Listener da lista agora chama o método para atualizar os detalhes.
        listaDeEventos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Evento eventoSelecionado = listaDeEventos.getSelectedValue();
                atualizarDetalhesEvento(eventoSelecionado); // ATUALIZADO!
            }
        });
        
        // NOVO! Painel de detalhes para substituir a JTextArea.
        JPanel painelDetalhes = new JPanel(new BorderLayout());
        painelDetalhes.setBorder(BorderFactory.createTitledBorder("Detalhes do Evento"));
        
        // Painel interno para organizar os labels
        JPanel painelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        valorDescricao = new JLabel();
        valorTempoRestante = new JLabel();
        
        painelCampos.add(new JLabel("Descrição:"));
        painelCampos.add(valorDescricao);
        painelCampos.add(new JLabel("Tempo Restante:"));
        painelCampos.add(valorTempoRestante);
        
        // Adiciona os campos ao painel de detalhes.
        painelDetalhes.add(painelCampos, BorderLayout.NORTH);

        // Carrega os eventos e inicializa os detalhes
        popularListaEventos();

        JSplitPane painelDividido = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaDeEventos),
                // REMOVIDO/SUBSTITUÍDO: Troca a área de descrição pelo novo painel.
                // new JScrollPane(areaDescricao));
                painelDetalhes); // NOVO!
        painelDividido.setDividerLocation(300);

        JPanel painelDeConteudo = new JPanel(new BorderLayout());
        painelDeConteudo.add(painelDividido, BorderLayout.CENTER);

        return painelDeConteudo;
    }

    // NOVO! Método que atualiza os detalhes na tela.
    private void atualizarDetalhesEvento(Evento evento) {
        if (evento != null) {
            valorDescricao.setText("<html>" + evento.getDescricao() + "</html>"); // Usa HTML para quebra de linha
            
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
        // NOVO! Limpa os detalhes ao popular a lista.
        atualizarDetalhesEvento(null);
    }

    private void adicionarNovoEvento() {
        Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(this);
        // MUDANÇA: O diálogo também precisará do orquestrador se ele realizar alguma validação.
        DialogoEvento dialogo = new DialogoEvento(framePrincipal, orquestrador);
        dialogo.setVisible(true);

        if (dialogo.foiSalvo()) {
            // MUDANÇA: O próprio diálogo já cadastrou o evento através do Orquestrador.
            // Apenas precisamos atualizar a nossa lista para refletir a mudança.
            popularListaEventos();
            // Opcional: selecionar o novo item, mas requer que o diálogo o retorne.
            // listaDeEventos.setSelectedValue(dialogo.getEvento(), true);
        }
    }

    private void editarEventoSelecionado() {
        Evento eventoSelecionado = listaDeEventos.getSelectedValue();
        if (eventoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Frame framePrincipal = (Frame) SwingUtilities.getWindowAncestor(this);
        // MUDANÇA: O diálogo de edição precisa do Orquestrador para salvar as alterações.
        DialogoEvento dialogo = new DialogoEvento(framePrincipal, orquestrador, eventoSelecionado);
        dialogo.setVisible(true);

        if (dialogo.foiSalvo()) {
            // MUDANÇA: O diálogo salvou a edição, então só precisamos atualizar a exibição.
            popularListaEventos();
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
            // MUDANÇA: Delega a exclusão para o Orquestrador.
            this.orquestrador.excluirEvento(eventoSelecionado);
            // MUDANÇA: Atualiza a lista após a exclusão.
            popularListaEventos();
        }
    }
}