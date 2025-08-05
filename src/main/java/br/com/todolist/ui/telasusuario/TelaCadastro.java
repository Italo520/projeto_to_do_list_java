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
    
    private final GerenteDeUsuarios gerenteDeUsuarios;

    public TelaCadastro(Frame owner, GerenteDeUsuarios gerenteDeUsuarios) {
        super(owner, "Criar Nova Conta", true);
        this.gerenteDeUsuarios = gerenteDeUsuarios;

        montarLayoutComGridBag();
        configurarAcoes();

        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private void montarLayoutComGridBag() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        campoNome = new JTextField(20);
        add(campoNome, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        campoEmail = new JTextField(20);
        add(campoEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        campoSenha = new JPasswordField(20);
        add(campoSenha, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botaoCadastrar = new JButton("Cadastrar");
        botaoCancelar = new JButton("Cancelar");

        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoCancelar);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(painelBotoes, gbc);
    }

    private void configurarAcoes() {
        botaoCancelar.addActionListener(e -> dispose());

        botaoCadastrar.addActionListener(e -> {
            String nome = campoNome.getText();
            String email = campoEmail.getText();
            String senha = new String(campoSenha.getPassword());

            if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean sucesso = gerenteDeUsuarios.criarNovoUsuario(nome, email, senha);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Este email já está em uso. Tente outro.", "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}