package br.com.todolist.ui.telasusuario;

import br.com.todolist.models.Usuario;
import br.com.todolist.service.GerenteDeUsuarios;
import br.com.todolist.ui.telaPrincipal.TelaPrincipal;

import javax.swing.*;

public class TelaLogin extends JFrame {

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JButton botaoCriarConta;
    private final GerenteDeUsuarios gerenteDeUsuarios;

    public TelaLogin() {
        super("Login - ToDo List");
        this.gerenteDeUsuarios = new GerenteDeUsuarios();
        configurarLayout();
        configurarAcoes();
    }
    
    private void configurarLayout() {
        // ... (seu método configurarLayout() continua aqui, sem alterações)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null); 

        // Campo de Email
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(440, 260, 100, 30);
        add(labelEmail);

        campoEmail = new JTextField();
        campoEmail.setBounds(550, 260, 250, 30);
        add(campoEmail); 
        
        // Campo de Senha
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(440, 305, 100, 30);
        add(labelSenha); 

        campoSenha = new JPasswordField();
        campoSenha.setBounds(550, 305, 250, 30);
        add(campoSenha);

        // Botoes
        botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBounds(550, 365, 120, 30);
        add(botaoEntrar);

        botaoCriarConta = new JButton("Criar Conta");
        botaoCriarConta.setBounds(680, 365, 120, 30);
        add(botaoCriarConta);
    }

    private void configurarAcoes() {
        botaoEntrar.addActionListener(new OuvinteBotaoEntrar());
        botaoCriarConta.addActionListener(new OuvinteBotaoCriarConta());
        campoSenha.addActionListener(new OuvinteBotaoEntrar());
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
            new TelaPrincipal(usuarioAutenticado).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class OuvinteBotaoEntrar implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            realizarLogin();
        }
    }

    private class OuvinteBotaoCriarConta implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            TelaCadastro telaCadastro = new TelaCadastro(TelaLogin.this, gerenteDeUsuarios);
            telaCadastro.setVisible(true); // O código para aqui até a tela de cadastro ser fechada

            // --- CÓDIGO NOVO AQUI ---
            // Após a tela de cadastro fechar, verificamos se um email foi retornado
            String emailNovo = telaCadastro.getEmailCadastrado();
            if (emailNovo != null) {
                campoEmail.setText(emailNovo); // Preenche o campo de email
                campoSenha.setText(""); // Limpa o campo de senha
                campoSenha.requestFocus(); // Coloca o foco no campo de senha para facilitar
            }
        }
    }
}