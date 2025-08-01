package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable; // Exemplo: usando uma JTable para listar tarefas

public class PainelTarefas extends PainelBase {
    
    @Override
    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botaoNovaTarefa = new JButton("Nova Tarefa");
        JButton botaoEditarTarefa = new JButton("Editar Tarefa");
        JButton botaoExcluirTarefa = new JButton("Excluir Tarefa");

        botaoNovaTarefa.addActionListener(e -> System.out.println("Abrir tela de nova tarefa..."));
        // Adicione os outros listeners

        painel.add(botaoNovaTarefa);
        painel.add(botaoEditarTarefa);
        painel.add(botaoExcluirTarefa);

        return painel;
    }

    @Override
    protected JPanel criarPainelDeConteudo() {
        JPanel painel = new JPanel(new BorderLayout());

        // Exemplo: Usando uma JTable para exibir as tarefas
        String[] colunas = {"ID", "Descrição", "Status", "Prazo"};
        Object[][] dados = {
            {1, "Implementar herança na UI", "Em andamento", "01/08/2025"},
            {2, "Refatorar código do backend", "Pendente", "15/08/2025"}
        };
        JTable tabelaTarefas = new JTable(dados, colunas);

        JScrollPane scrollPane = new JScrollPane(tabelaTarefas);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }
}