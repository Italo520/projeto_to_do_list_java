package main.java.br.com.todolist.ui.telasusuario;


import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import br.com.todolist.service.GerenteDeUsuarios;

public class TelaCadastro extends JDialog {

    private JLabel labelNome, labelEmail, labelSenha;
    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JButton botaoCadastrar;
    private JButton botaoCancelar;
    private GerenteDeUsuarios gerenteDeUsuarios;

    public TelaCadastro(Frame owner) {
        super(owner, "Criar Nova Conta", true);
        this.gerenteDeUsuarios = new GerenteDeUsuarios();

        // 1. Definindo o layout como nulo
        setLayout(null);
        
        montarLayout();
        configurarAcoes();

        // 2. Definindo um tamanho fixo para a janela, já que pack() não funciona bem com layout nulo
        setSize(350, 220);
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private void montarLayout() {
        // Rótulos
        labelNome = new JLabel("Nome:");
        labelNome.setBounds(20, 20, 80, 25); // (x, y, largura, altura)
        add(labelNome);

        labelEmail = new JLabel("Email:");
        labelEmail.setBounds(20, 60, 80, 25);
        add(labelEmail);

        labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(20, 100, 80, 25);
        add(labelSenha);

        // Campos de Texto
        campoNome = new JTextField();
        campoNome.setBounds(100, 20, 210, 25);
        add(campoNome);

        campoEmail = new JTextField();
        campoEmail.setBounds(100, 60, 210, 25);
        add(campoEmail);

        campoSenha = new JPasswordField();
        campoSenha.setBounds(100, 100, 210, 25);
        add(campoSenha);
        
        // Botões
        botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setBounds(100, 140, 100, 30);
        add(botaoCadastrar);

        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setBounds(210, 140, 100, 30);
        add(botaoCancelar);
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