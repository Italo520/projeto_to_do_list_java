// Crie este novo arquivo em: src/main/java/br/com/todolist/ui/telaPrincipal/DialogoEvento.java

package br.com.todolist.ui.TelasDialogo;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.com.todolist.models.Evento;

public class DialogoEvento extends JDialog {

    private JTextField campoTitulo;
    private JTextField campoDescricao;
    private JTextField campoData; // Formato: dd/MM/yyyy

    private JButton botaoSalvar;
    private JButton botaoCancelar;

    private Evento evento;
    private boolean salvo = false;
    private DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor para criar um NOVO evento.
     */
    public DialogoEvento(Frame owner) {
        super(owner, "Novo Evento", true);
        this.evento = null;
        montarLayout();
        configurarAcoes();
        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * Construtor para EDITAR um evento existente.
     */
    public DialogoEvento(Frame owner, Evento eventoParaEditar) {
        super(owner, "Editar Evento", true);
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

        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        campoTitulo = new JTextField(25);
        add(campoTitulo, gbc);

        // Descrição
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        campoDescricao = new JTextField(25);
        add(campoDescricao, gbc);

        // Data do Evento
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        add(new JLabel("Data (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        campoData = new JTextField(10);
        add(campoData, gbc);

        // Botões
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        botaoSalvar = new JButton("Salvar");
        add(botaoSalvar, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        botaoCancelar = new JButton("Cancelar");
        add(botaoCancelar, gbc);
    }

    private void preencherCampos() {
        if (evento != null) {
            campoTitulo.setText(evento.getTitulo());
            campoDescricao.setText(evento.getDescricao());
            campoData.setText(evento.getDataEvento().format(formatadorDeData));
        }
    }

    private void configurarAcoes() {
        botaoCancelar.addActionListener(e -> dispose());

        botaoSalvar.addActionListener(e -> {
            if (validarCampos()) {
                String titulo = campoTitulo.getText();
                String descricao = campoDescricao.getText();
                LocalDate dataEvento = LocalDate.parse(campoData.getText(), formatadorDeData);

                if (this.evento == null) {
                    // Para um novo evento, a data de cadastro é hoje.
                    this.evento = new Evento(titulo, descricao, LocalDate.now(), dataEvento);
                } else {
                    this.evento.setTitulo(titulo);
                    this.evento.setDescricao(descricao);
                    this.evento.setDataEvento(dataEvento);
                }
                
                this.salvo = true;
                dispose();
            }
        });
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

    public Evento getEvento() {
        return this.evento;
    }

    public boolean foiSalvo() {
        return this.salvo;
    }
}