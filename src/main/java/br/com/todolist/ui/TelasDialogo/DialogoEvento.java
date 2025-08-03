// Em: src/main/java/br/com/todolist/ui/TelasDialogo/DialogoEvento.java
package br.com.todolist.ui.TelasDialogo;

import br.com.todolist.models.Evento;
import br.com.todolist.service.Orquestrador;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DialogoEvento extends JDialog {

    private final Orquestrador orquestrador;
    private Evento evento;

    private JTextField campoTitulo;
    private JTextField campoDescricao;
    private JTextField campoData;

    private JButton botaoSalvar;
    private JButton botaoCancelar;

    private boolean salvo = false;
    private final DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Construtor para um novo evento
    public DialogoEvento(Frame owner, Orquestrador orquestrador) {
        super(owner, "Novo Evento", true);
        this.orquestrador = orquestrador;
        this.evento = null;
        montarLayout();
        configurarAcoes();
        pack();
        setLocationRelativeTo(owner);
    }

    // Construtor para editar um evento existente
    public DialogoEvento(Frame owner, Orquestrador orquestrador, Evento eventoParaEditar) {
        super(owner, "Editar Evento", true);
        this.orquestrador = orquestrador;
        this.evento = eventoParaEditar;
        montarLayout();
        preencherCampos();
        configurarAcoes();
        pack();
        setLocationRelativeTo(owner);
    }

    private void montarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        campoTitulo = new JTextField(25); add(campoTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        campoDescricao = new JTextField(25); add(campoDescricao, gbc);

        // O label foi alterado para refletir o conceito de 'Deadline'
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; add(new JLabel("Deadline (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        campoData = new JTextField(10); add(campoData, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoSalvar = new JButton("Salvar");
        botaoCancelar = new JButton("Cancelar");
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCancelar);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.NONE;
        add(painelBotoes, gbc);
    }

    private void preencherCampos() {
        if (evento != null) {
            campoTitulo.setText(evento.getTitulo());
            campoDescricao.setText(evento.getDescricao());
            // CORREÇÃO: Utilizando getDeadline() ao invés de getDataEvento()
            campoData.setText(evento.getDeadline().format(formatadorDeData));
        }
    }

    private void configurarAcoes() {
        botaoCancelar.addActionListener(e -> dispose());
        botaoSalvar.addActionListener(e -> salvar());
    }

    private void salvar() {
        if (!validarCampos()) {
            return;
        }

        String titulo = campoTitulo.getText();
        String descricao = campoDescricao.getText();
        // A variável agora se chama 'deadline' para maior clareza
        LocalDate deadline = LocalDate.parse(campoData.getText(), formatadorDeData);

        try {
            if (this.evento == null) {
                // A variável 'deadline' é passada para o construtor e para o método do orquestrador
                Evento novoEvento = new Evento(titulo, descricao, deadline);
                boolean sucesso = orquestrador.cadastrarEvento(novoEvento);
                if (!sucesso) {
                     JOptionPane.showMessageDialog(this, "Não foi possível cadastrar o evento.\nVerifique se já não existe um evento com o mesmo título.", "Erro ao Cadastrar", JOptionPane.WARNING_MESSAGE);
                     return;
                }
            } else {
                orquestrador.editarEvento(this.evento, titulo, descricao, deadline);
            }
            
            this.salvo = true;
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao salvar o evento:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (campoTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo 'Título' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            LocalDate.parse(campoData.getText(), formatadorDeData);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "O formato da data é inválido. Use dd/MM/yyyy.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean foiSalvo() {
        return this.salvo;
    }
}