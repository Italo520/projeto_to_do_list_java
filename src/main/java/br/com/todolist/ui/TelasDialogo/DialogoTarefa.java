// Crie este novo arquivo em: src/main/java/br/com/todolist/ui/telaPrincipal/DialogoTarefa.java

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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import br.com.todolist.models.Tarefa;

public class DialogoTarefa extends JDialog {

    private JTextField campoTitulo;
    private JTextField campoDescricao;
    private JSpinner campoPrioridade;
    private JTextField campoPrazo; // Para simplicidade, usaremos texto. Formato: dd/MM/yyyy

    private JButton botaoSalvar;
    private JButton botaoCancelar;

    private Tarefa tarefa;
    private boolean salvo = false;
    private DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor para criar uma NOVA tarefa.
     */
    public DialogoTarefa(Frame owner) {
        super(owner, "Nova Tarefa", true); // O 'true' torna o diálogo modal
        this.tarefa = null;
        montarLayout();
        configurarAcoes();
        pack(); // Ajusta o tamanho da janela ao conteúdo
        setLocationRelativeTo(owner); // Centraliza em relação à janela principal
    }

    /**
     * Construtor para EDITAR uma tarefa existente.
     */
    public DialogoTarefa(Frame owner, Tarefa tarefaParaEditar) {
        super(owner, "Editar Tarefa", true);
        this.tarefa = tarefaParaEditar;
        montarLayout();
        preencherCampos();
        configurarAcoes();
        pack();
        setLocationRelativeTo(owner);
    }
    
    private void montarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento
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

        // Prioridade
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        add(new JLabel("Prioridade:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        campoPrioridade = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1)); // Valor inicial 1, min 1, max 5, passo 1
        add(campoPrioridade, gbc);

        // Prazo
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Prazo (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        campoPrazo = new JTextField(10);
        add(campoPrazo, gbc);

        // Botões
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        botaoSalvar = new JButton("Salvar");
        add(botaoSalvar, gbc);

        gbc.gridx = 2; gbc.gridy = 4;
        botaoCancelar = new JButton("Cancelar");
        add(botaoCancelar, gbc);
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
        botaoCancelar.addActionListener(e -> dispose()); // Simplesmente fecha a janela

        botaoSalvar.addActionListener(e -> {
            if (validarCampos()) {
                // Pega os valores dos campos
                String titulo = campoTitulo.getText();
                String descricao = campoDescricao.getText();
                int prioridade = (int) campoPrioridade.getValue();
                LocalDate prazo = LocalDate.parse(campoPrazo.getText(), formatadorDeData);

                // Se estamos editando, atualiza o objeto existente. Se não, cria um novo.
                if (this.tarefa == null) {
                    this.tarefa = new Tarefa(titulo, descricao, prazo, prioridade);
                } else {
                    this.tarefa.setTitulo(titulo);
                    this.tarefa.setDescricao(descricao);
                    this.tarefa.setPrioridade(prioridade);
                    this.tarefa.setDeadline(prazo);
                }
                
                this.salvo = true;
                dispose(); // Fecha a janela
            }
        });
    }

    private boolean validarCampos() {
        if (campoTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo 'Título' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            LocalDate.parse(campoPrazo.getText(), formatadorDeData);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "O formato da data para o 'Prazo' é inválido. Use dd/MM/yyyy.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Métodos públicos para o PainelTarefas obter o resultado
    public Tarefa getTarefa() {
        return this.tarefa;
    }

    public boolean foiSalvo() {
        return this.salvo;
    }
}