package br.com.todolist.ui.TelasDialogo;

import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DialogoTarefa extends JDialog {

    private final Orquestrador orquestrador;
    private Tarefa tarefa;

    private JTextField campoTitulo;
    private JTextField campoDescricao;
    private JSpinner campoPrioridade;
    private JTextField campoPrazo;

    private JButton botaoSalvar;
    private JButton botaoCancelar;

    private boolean salvo = false;
    private final DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Construtores
    public DialogoTarefa(Frame frame, Orquestrador orquestrador) {
        super(frame, "Nova Tarefa", true);
        this.orquestrador = orquestrador;
        this.tarefa = null;
        configurarEAdicionarComponentes();
        configurarAcoes();
    }

    public DialogoTarefa(Frame frame, Orquestrador orquestrador, Tarefa tarefaParaEditar) {
        super(frame, "Editar Tarefa", true);
        this.orquestrador = orquestrador;
        this.tarefa = tarefaParaEditar;
        configurarEAdicionarComponentes();
        preencherCampos();
        configurarAcoes();
    }

    private void configurarEAdicionarComponentes() {
        setTitle(tarefa == null ? "Nova Tarefa" : "Editar Tarefa");
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

        JLabel labelPrioridade = new JLabel("Prioridade:");
        labelPrioridade.setBounds(400, 320, 100, 30);
        add(labelPrioridade);

        campoPrioridade = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        campoPrioridade.setBounds(600, 320, 70, 30);
        add(campoPrioridade);

        JLabel labelPrazo = new JLabel("Prazo (dd/MM/yyyy):");
        labelPrazo.setBounds(400, 365, 150, 30);
        add(labelPrazo);

        campoPrazo = new JTextField();
        campoPrazo.setBounds(600, 365, 400, 30);
        add(campoPrazo);

        botaoSalvar = new JButton("Salvar");
        botaoSalvar.setBounds(600, 425, 120, 30);
        add(botaoSalvar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(750, 425, 120, 30);
        add(botaoCancelar);
    }
    
    private void preencherCampos() {
        if (tarefa != null) {
            campoTitulo.setText(tarefa.getTitulo());
            campoDescricao.setText(tarefa.getDescricao());
            campoPrioridade.setValue(tarefa.getPrioridade());
            campoPrazo.setText(tarefa.getDeadline().format(formatadorDeData));
        }
    }

    private void configurarAcoes() {
        botaoCancelar.addActionListener(e -> dispose());
        botaoSalvar.addActionListener(e -> salvar());
    }

    private void salvar() {
        try {
            String titulo = campoTitulo.getText().trim();
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Título' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String descricao = campoDescricao.getText().trim();
            int prioridade = (int) campoPrioridade.getValue();
            LocalDate prazo = LocalDate.parse(campoPrazo.getText(), formatadorDeData);

            if (this.tarefa == null) {
                orquestrador.cadastrarTarefa(titulo, descricao, prazo, prioridade);
            } else {
                orquestrador.editarTarefa(this.tarefa, titulo, descricao, prazo, prioridade);
            }

            this.salvo = true;
            dispose();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean foiSalvo() {
        return this.salvo;
    }
}