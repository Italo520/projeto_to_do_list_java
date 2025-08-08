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

    public TelaCadastro(Frame telalogin, GerenteDeUsuarios gerenteDeUsuarios) {
        super(telalogin, "Criar Nova Conta", true);
        this.gerenteDeUsuarios = gerenteDeUsuarios;

        configurarLayout();
        configurarAcoes();
        setVisible(true);
    }

    private void configurarLayout() {
        setTitle("Criar Nova Conta");
        setSize(1280, 720);
        setResizable(false);
        setLayout(null);

        // Campo Nome
        JLabel labelNome = new JLabel("Nome:");
        labelNome.setBounds(440, 230, 100, 30);
        add(labelNome);

        campoNome = new JTextField();
        campoNome.setBounds(550, 230, 250, 30);
        add(campoNome);

        // Campo Email
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(440, 275, 100, 30);
        add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(550, 275, 250, 30);
        add(campoEmail);

        // Campo Senha
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 320, 100, 30);
        add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(550, 320, 250, 30);
        add(campoSenha);

        // Botoes
        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBounds(550, 380, 120, 30);
        add(botaoCadastrar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(680, 380, 120, 30);
        add(botaoCancelar);
    }

    private void configurarAcoes() {
        botaoCadastrar.addActionListener(new CadastrarAction());
        botaoCancelar.addActionListener(new OuvinteBotaoCancelar());
    }


    private class CadastrarAction implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            String nome = campoNome.getText();
            String email = campoEmail.getText();
            String senha = new String(campoSenha.getPassword());

            if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
                JOptionPane.showMessageDialog(TelaCadastro.this, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean sucesso = gerenteDeUsuarios.criarNovoUsuario(nome, email, senha);

            if (sucesso) {
                JOptionPane.showMessageDialog(TelaCadastro.this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(TelaCadastro.this, "Este email já está em uso. Tente outro.", "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private class OuvinteBotaoCancelar implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            dispose();
        }
    }
}