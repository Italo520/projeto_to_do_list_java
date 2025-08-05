package br.com.todolist.ui.telasusuario;

import br.com.todolist.service.GerenteDeUsuarios;

import javax.swing.*;
import java.awt.*;

public class TelaCadastro extends JDialog {

    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoCadastrar;
    private JButton botaoCancelar;
    
    // 1. A dependência agora é recebida, não criada aqui.
    private final GerenteDeUsuarios gerenteDeUsuarios;

    // Construtor atualizado para receber a dependência
    public TelaCadastro(Frame owner, GerenteDeUsuarios gerenteDeUsuarios) {
        super(owner, "Criar Nova Conta", true);
        this.gerenteDeUsuarios = gerenteDeUsuarios;

        montarLayoutComGridBag(); // Usando o novo método de layout
        configurarAcoes();

        pack(); // 2. pack() ajusta o tamanho da janela ao conteúdo. É o ideal com LayoutManagers.
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private void montarLayoutComGridBag() {
        // Usando GridBagLayout para um formulário flexível e robusto
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Insets definem uma margem interna para cada componente (top, left, bottom, right)
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz os componentes preencherem o espaço horizontal

        // Linha 0: Rótulo Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST; // Alinha o rótulo à direita
        add(new JLabel("Nome:"), gbc);

        // Linha 0: Campo Nome
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa 2 colunas
        gbc.anchor = GridBagConstraints.WEST; // Alinha o campo à esquerda
        campoNome = new JTextField(20); // O '20' sugere um tamanho preferencial
        add(campoNome, gbc);
        
        // Linha 1: Rótulo Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reseta para 1 coluna
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Email:"), gbc);

        // Linha 1: Campo Email
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        campoEmail = new JTextField(20);
        add(campoEmail, gbc);

        // Linha 2: Rótulo Senha
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Senha:"), gbc);

        // Linha 2: Campo Senha
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        campoSenha = new JPasswordField(20);
        add(campoSenha, gbc);

        // Linha 3: Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Alinha botões à direita
        botaoCadastrar = new JButton("Cadastrar");
        botaoCancelar = new JButton("Cancelar");
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoCancelar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3; // Ocupa 3 colunas
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(painelBotoes, gbc);
    }

    private void configurarAcoes() {
        // A lógica de ações permanece a mesma, pois já estava boa!
        botaoCancelar.addActionListener(e -> dispose());

        botaoCadastrar.addActionListener(e -> {
            String nome = campoNome.getText();
            String email = campoEmail.getText();
            String senha = new String(campoSenha.getPassword());

            if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // A chamada ao gerente agora usa a instância injetada
            boolean sucesso = gerenteDeUsuarios.criarNovoUsuario(nome, email, senha);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Fecha a tela de cadastro
            } else {
                // A sua classe GerenteDeUsuarios imprime o erro no console.
                // Uma mensagem genérica aqui é mais segura.
                JOptionPane.showMessageDialog(this, "Este email já está em uso. Tente outro.", "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}