// Localização: src/br/com/poo/todolist/ui/dialogs/NovaTarefaDialog.java
package br.com.poo.todolist.ui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class NovaTarefaDialog extends JDialog {

    // --- Componentes da Interface ---
    private JTextArea campoDescricao;
    private JSpinner spinnerDataEntrega; // Um seletor de data padrão do Swing
    private JComboBox<String> comboPrioridade;
    private JComboBox<String> comboCategoria;
    private JButton botaoSalvar;
    private JButton botaoCancelar;

    // Construtor do Dialog
    public NovaTarefaDialog(Frame owner) {
        // Configurações básicas do JDialog
        super(owner, "Adicionar Nova Tarefa", true); // true = modal
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Define o layout principal do dialog
        setLayout(new BorderLayout(10, 10)); // Espaçamento de 10px

        // --- Cria e adiciona os painéis ---
        add(criarPainelPrincipal(), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);

        // Ajusta o tamanho da janela e a centraliza
        pack();
        setMinimumSize(new Dimension(500, 400)); // Define um tamanho mínimo
        setLocationRelativeTo(owner);
    }

    /**
     * Cria o painel central com todos os campos de entrada.
     * Usamos GridBagLayout para máxima flexibilidade.
     */
    private JPanel criarPainelPrincipal() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz os componentes preencherem o espaço horizontal

        // --- Linha 0: Label "Descrição" ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa duas colunas
        gbc.anchor = GridBagConstraints.WEST; // Alinha à esquerda
        painel.add(new JLabel("Descrição:"), gbc);

        // --- Linha 1: Campo de Texto "Descrição" com Scroll ---
        campoDescricao = new JTextArea(5, 20); // 5 linhas, 20 colunas
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        gbc.gridy = 1;
        gbc.weightx = 1.0; // Permite crescer na horizontal
        gbc.weighty = 1.0; // Permite crescer na vertical
        gbc.fill = GridBagConstraints.BOTH; // Preenche ambos os espaços
        painel.add(scrollDescricao, gbc);

        // --- Linha 2: Painel de Detalhes (Data, Prioridade, Categoria) ---
        gbc.gridy = 2;
        gbc.weighty = 0; // Não cresce na vertical
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(criarPainelDetalhes(), gbc);

        return painel;
    }

    /**
     * Cria o painel inferior com os campos de data, prioridade e categoria.
     */
    private JPanel criarPainelDetalhes() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Data de Entrega
        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(new JLabel("Data de Entrega:"), gbc);

        gbc.gridx = 1;
        // Usamos JSpinner para um seletor de data nativo
        spinnerDataEntrega = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDataEntrega, "dd/MM/yyyy");
        spinnerDataEntrega.setEditor(dateEditor);
        spinnerDataEntrega.setValue(new Date()); // Valor inicial: data de hoje
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        painel.add(spinnerDataEntrega, gbc);

        // Prioridade
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painel.add(new JLabel("Prioridade:"), gbc);

        gbc.gridx = 1;
        String[] prioridades = {"Baixa", "Média", "Alta", "Urgente"};
        comboPrioridade = new JComboBox<>(prioridades);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        painel.add(comboPrioridade, gbc);

        // Categoria
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painel.add(new JLabel("Categoria:"), gbc);

        gbc.gridx = 1;
        String[] categorias = {"Trabalho", "Pessoal", "Estudos"};
        comboCategoria = new JComboBox<>(categorias);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        painel.add(comboCategoria, gbc);

        return painel;
    }

    /**
     * Cria o painel do rodapé com os botões "Salvar" e "Cancelar".
     */
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Alinhado à direita
        botaoSalvar = new JButton("Salvar");
        botaoCancelar = new JButton("Cancelar");

        // Define a ação do botão Cancelar (apenas fecha o dialog)
        botaoCancelar.addActionListener(e -> dispose());

        // Ação do botão Salvar (você adicionará a lógica de salvar aqui)
        botaoSalvar.addActionListener(e -> {
            // Lógica para pegar os dados dos campos e criar o objeto Tarefa
            String descricao = campoDescricao.getText();
            Date dataEntrega = (Date) spinnerDataEntrega.getValue();
            String prioridade = (String) comboPrioridade.getSelectedItem();
            String categoria = (String) comboCategoria.getSelectedItem();

            System.out.println("Salvando Tarefa:");
            System.out.println("Descrição: " + descricao);
            System.out.println("Data: " + dataEntrega);
            System.out.println("Prioridade: " + prioridade);
            System.out.println("Categoria: " + categoria);

            // TODO: Criar o objeto Tarefa e retorná-lo para a tela principal

            // Fecha a janela após salvar
            dispose();
        });

        painel.add(botaoCancelar);
        painel.add(botaoSalvar);
        return painel;
    }

    /**
     * Método main para testar o dialog de forma independente.
     */
    public static void main(String[] args) {
        // Cria e exibe o dialog para teste rápido
        // Passamos 'null' como pai, pois não há janela principal neste teste
        NovaTarefaDialog dialog = new NovaTarefaDialog(null);
        dialog.setVisible(true);

        // O programa termina quando o dialog é fechado
        System.exit(0);
    }
}