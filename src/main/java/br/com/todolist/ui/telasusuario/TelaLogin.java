package br.com.todolist.ui.telasusuario;

import br.com.todolist.models.Usuario;
import br.com.todolist.service.GerenteDeUsuarios;
import br.com.todolist.ui.telaPrincipal.TelaPrincipal;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JButton botaoCriarConta;

    // 1. O GerenteDeUsuarios é criado UMA VEZ aqui e se torna a fonte da verdade para a aplicação.
    private final GerenteDeUsuarios gerenteDeUsuarios;

    public TelaLogin() {
        super("Login - ToDo List");
        this.gerenteDeUsuarios = new GerenteDeUsuarios(); // Instância única

        montarLayoutComGridBag();
        configurarAcoes(); // Código de ações agora em um método separado

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Ajusta o tamanho da janela ao conteúdo
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void montarLayoutComGridBag() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Rótulo e Campo Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        campoEmail = new JTextField(20);
        add(campoEmail, gbc);

        // Linha 1: Rótulo e Campo Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        campoSenha = new JPasswordField(20);
        add(campoSenha, gbc);

        // Linha 2: Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botaoEntrar = new JButton("Entrar");
        botaoCriarConta = new JButton("Criar Conta");
        painelBotoes.add(botaoEntrar);
        painelBotoes.add(botaoCriarConta);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // O painel ocupa duas colunas
        gbc.anchor = GridBagConstraints.CENTER;
        add(painelBotoes, gbc);
    }

    private void configurarAcoes() {
        botaoEntrar.addActionListener(e -> realizarLogin());

        botaoCriarConta.addActionListener(e -> {

            TelaCadastro telaCadastro = new TelaCadastro(this, this.gerenteDeUsuarios);
            telaCadastro.setVisible(true);
        });


        campoSenha.addActionListener(e -> realizarLogin());
    }

    private void realizarLogin() {
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        if (email.trim().isEmpty() || senha.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email e senha são obrigatórios.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuarioAutenticado = gerenteDeUsuarios.autenticarUsuario(email, senha);

        if (usuarioAutenticado != null) {
        TelaPrincipal telaPrincipal = new TelaPrincipal(usuarioAutenticado);
        this.dispose();
        telaPrincipal.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}