// Em: src/main/java/br/com/todolist/ui/TelasDialogo/DialogoTarefa.java
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
    private Tarefa tarefa; // Opcional, nulo para nova tarefa, preenchido para edição

    private JTextField campoTitulo;
    private JTextField campoDescricao;
    private JSpinner campoPrioridade;
    private JTextField campoPrazo;

    private JButton botaoSalvar;
    private JButton botaoCancelar;

    private boolean salvo = false;
    private final DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Construtor para NOVA tarefa
    public DialogoTarefa(Frame owner, Orquestrador orquestrador) {
        super(owner, "Nova Tarefa", true);
        this.orquestrador = orquestrador;
        this.tarefa = null; // Indica que é uma criação
        montarLayout();
        configurarAcoes();
        pack();
        setLocationRelativeTo(owner);
    }

    // Construtor para EDITAR tarefa
    public DialogoTarefa(Frame owner, Orquestrador orquestrador, Tarefa tarefaParaEditar) {
        super(owner, "Editar Tarefa", true);
        this.orquestrador = orquestrador;
        this.tarefa = tarefaParaEditar; // Guarda a referência da tarefa a ser editada
        montarLayout();
        preencherCampos();
        configurarAcoes();
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean foiSalvo() {
        return this.salvo;
    }

    private void montarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        campoTitulo = new JTextField(25);
        add(campoTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        campoDescricao = new JTextField(25);
        add(campoDescricao, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("Prioridade:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        campoPrioridade = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        add(campoPrioridade, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Prazo (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        campoPrazo = new JTextField(10);
        add(campoPrazo, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoSalvar = new JButton("Salvar");
        botaoCancelar = new JButton("Cancelar");
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCancelar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        add(painelBotoes, gbc);
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
        botaoCancelar.addActionListener(e -> dispose()); // dispose() fecha a janela
        botaoSalvar.addActionListener(e -> salvar());
    }

    private void salvar() {
        try {
            // 1. Coleta e valida os dados do formulário
            String titulo = campoTitulo.getText().trim();
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Título' é obrigatório.", "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String descricao = campoDescricao.getText().trim();
            int prioridade = (int) campoPrioridade.getValue();
            LocalDate prazo = LocalDate.parse(campoPrazo.getText(), formatadorDeData);

            // 2. Decide se é uma nova tarefa ou uma edição
            if (this.tarefa == null) { // Criando uma nova tarefa
                // CORREÇÃO: Chama o novo método do orquestrador com os parâmetros
                orquestrador.cadastrarTarefa(titulo, descricao, prazo, prioridade);
            } else { // Editando uma tarefa existente
                orquestrador.editarTarefa(this.tarefa, titulo, descricao, prazo, prioridade);
            }

            // 3. Sinaliza que a operação foi bem-sucedida e fecha o diálogo
            this.salvo = true;
            dispose();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}