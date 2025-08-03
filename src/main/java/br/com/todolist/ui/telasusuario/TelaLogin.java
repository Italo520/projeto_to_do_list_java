// Em: src/main/java/br/com/todolist/ui/telasUsuario/TelaLogin.java
package br.com.todolist.ui.telasUsuario;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import br.com.todolist.models.Usuario;
import br.com.todolist.service.GerenteDeUsuarios;
import br.com.todolist.ui.telaPrincipal.TelaPrincipal;

public class TelaLogin extends JFrame {

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private GerenteDeUsuarios gerenteDeUsuarios;

    public TelaLogin() {
        super("Login - ToDo List");
        this.gerenteDeUsuarios = new GerenteDeUsuarios();
        
        setLayout(null); // Usando layout nulo conforme solicitado
        montarLayout();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void montarLayout() {
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setBounds(30, 30, 80, 25);
        add(labelEmail);

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(30, 70, 80, 25);
        add(labelSenha);

        campoEmail = new JTextField();
        campoEmail.setBounds(120, 30, 230, 25);
        add(campoEmail);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(120, 70, 230, 25);
        add(campoSenha);

        JButton botaoEntrar = new JButton("Entrar");
        botaoEntrar.setBounds(120, 110, 100, 30);
        add(botaoEntrar);
        
        // AÇÃO DE DIRECIONAMENTO ESTÁ AQUI
        botaoEntrar.addActionListener(e -> {
            String email = campoEmail.getText();
            String senha = new String(campoSenha.getPassword());

            Usuario usuarioAutenticado = gerenteDeUsuarios.autenticarUsuario(email, senha);

            if (usuarioAutenticado != null) {
                // SUCESSO: Fecha a tela de login
                dispose(); 
                // CRIA E EXIBE A TELA PRINCIPAL, passando o usuário válido
                new TelaPrincipal(usuarioAutenticado).setVisible(true);
            } else {
                // FALHA: Mostra uma mensagem de erro
                JOptionPane.showMessageDialog(this, "Email ou senha incorretos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton botaoCriarConta = new JButton("Criar Conta");
        botaoCriarConta.setBounds(230, 110, 120, 30);
        add(botaoCriarConta);
        botaoCriarConta.addActionListener(e -> {
            new TelaCadastro(this).setVisible(true);
        });
    }
}