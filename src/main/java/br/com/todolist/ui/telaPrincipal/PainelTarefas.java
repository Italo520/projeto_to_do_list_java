package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class PainelTarefas extends PainelBase {
    private JButton botaoNovaTarefa;
    private JButton botaoEditarTarefa;
    private JButton botaoExcluirTarefa;

    
    protected void inicializarPaineis() {
        setLayout(new BorderLayout());

        JPanel painelDeBotoes = criarPainelDeBotoes();
        add(painelDeBotoes, BorderLayout.NORTH);

        JPanel painelDeConteudo = criarPainelDeConteudo();
        add(painelDeConteudo, BorderLayout.CENTER);
    
    }


    private JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel();
        
        painel.setLayout(new FlowLayout(FlowLayout.CENTER));


        botaoNovaTarefa = new JButton("Nova Tarefa");
        botaoEditarTarefa = new JButton("Editar Tarefa");
        botaoExcluirTarefa = new JButton("Excluir Tarefa");


        botaoNovaTarefa.addActionListener(e -> System.out.println("Botão 'Nova Tarefa' clicado!"));
        botaoEditarTarefa.addActionListener(e -> System.out.println("Botão 'Editar Tarefa' clicado!"));
        botaoExcluirTarefa.addActionListener(e -> System.out.println("Botão 'Excluir Tarefa' clicado!"));


        painel.add(botaoNovaTarefa);
        painel.add(botaoEditarTarefa);
        painel.add(botaoExcluirTarefa);

        return painel;
    }

    private JPanel criarPainelDeConteudo() {
        JPanel painel = new JPanel();

        JScrollPane scrollPane = new JScrollPane(new JLabel("", SwingConstants.CENTER));
        painel.setLayout(new BorderLayout());
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }



}
