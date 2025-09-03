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

    public DialogoEvento(Frame frame, Orquestrador orquestrador) {
        super(frame, "Novo Evento", true);
        this.orquestrador = orquestrador;
        this.evento = null;
        configurarEAdicionarComponentes();
        configurarAcoes();
    }

    public DialogoEvento(Frame frame, Orquestrador orquestrador, Evento eventoParaEditar) {
        super(frame, "Editar Evento", true);
        this.orquestrador = orquestrador;
        this.evento = eventoParaEditar;
        configurarEAdicionarComponentes();
        preencherCampos();
        configurarAcoes();
    }
    
    private void configurarEAdicionarComponentes() {
        setTitle(evento == null ? "Novo Evento" : "Editar Evento");
        setSize(1280, 720);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel labelTitulo = new JLabel("Título:");
        labelTitulo.setBounds(400, 230, 100, 30);
        add(labelTitulo);

        campoTitulo = new JTextField();
        campoTitulo.setBounds(600, 230, 400 , 30);
        add(campoTitulo);

        JLabel labelDescricao = new JLabel("Descrição:");
        labelDescricao.setBounds(400, 275, 100, 30);
        add(labelDescricao);

        campoDescricao = new JTextField();
        campoDescricao.setBounds(600, 275, 400, 30);
        add(campoDescricao);

        JLabel labelPrazo = new JLabel("Deadline (dd/MM/yyyy):");
        labelPrazo.setBounds(400, 320, 150, 30);
        add(labelPrazo);

        campoData = new JTextField();
        campoData.setBounds(600, 320, 400, 30);
        add(campoData);

        botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(600, 425, 120, 30);
        add(botaoSalvar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(750, 425, 120, 30);
        add(botaoCancelar);
    }
    
    private void preencherCampos() {
        if (evento != null) {
            campoTitulo.setText(evento.getTitulo());
            campoDescricao.setText(evento.getDescricao());
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
        LocalDate deadline = LocalDate.parse(campoData.getText(), formatadorDeData);

        try {
            if (this.evento == null) {
                boolean sucesso = orquestrador.cadastrarEvento(titulo, descricao, deadline);
                if (!sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "Não foi possível cadastrar o evento.\nVerifique se já não existe um evento com a mesma data para o seu usuário.",
                            "Erro ao Cadastrar", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                orquestrador.editarEvento(this.evento, titulo, descricao, deadline);
            }

            this.salvo = true;
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao salvar o evento:\n" + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (campoTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo 'Título' é obrigatório.", "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            LocalDate.parse(campoData.getText(), formatadorDeData);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "O formato da data é inválido. Use dd/MM/yyyy.", "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean foiSalvo() {
        return this.salvo;
    }
}