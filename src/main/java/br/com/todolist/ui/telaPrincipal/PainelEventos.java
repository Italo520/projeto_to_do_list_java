// Conteúdo para: src/main/java/br/com/todolist/ui/telaPrincipal/PainelEventos.java
package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Evento;
import br.com.todolist.service.Orquestrador; // MUDANÇA: Import do Orquestrador
import br.com.todolist.ui.TelasDialogo.DialogoEvento;

import javax.swing.*;
import java.awt.*;

public class PainelEventos extends PainelBase {

    // MUDANÇA: O Orquestrador é a nova fonte da verdade para os dados.
    private final Orquestrador orquestrador;

    private DefaultListModel<Evento> modeloListaEventos;
    private JList<Evento> listaDeEventos;
    private JTextArea areaDescricao;

    // MUDANÇA: Construtor agora recebe o Orquestrador (Injeção de Dependência).
    public PainelEventos(Orquestrador orquestrador) {
        this.orquestrador = orquestrador;
        // O método buildPanel() (da classe base) chamará os métodos de criação abaixo.
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

        // MUDANÇA: Carrega os eventos reais do Orquestrador ao iniciar.
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
        // MUDANÇA: Busca a lista de eventos sempre do Orquestrador.
        for (Evento evento : this.orquestrador.listarTodosEventos()) {
            modeloListaEventos.addElement(evento);
        }
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