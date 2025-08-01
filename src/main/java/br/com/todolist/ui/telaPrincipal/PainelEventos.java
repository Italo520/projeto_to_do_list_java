package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList; 

public class PainelEventos extends PainelBase {

    @Override
    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton botaoNovoEvento = new JButton("Novo Evento");
        JButton botaoEditarEvento = new JButton("Editar Evento");
        JButton botaoExcluirEvento = new JButton("Excluir Evento");

        botaoNovoEvento.addActionListener(e -> System.out.println("Abrir tela de novo evento..."));

        painel.add(botaoNovoEvento);
        painel.add(botaoEditarEvento);
        painel.add(botaoExcluirEvento);

        return painel;
    }

    @Override
    protected JPanel criarPainelDeConteudo() {
        JPanel painel = new JPanel(new BorderLayout());

        String[] eventos = {"Reunião de equipe - 10:00", "Aniversário do projeto - 15:00", "Workshop de Swing - 18:00"};
        JList<String> listaEventos = new JList<>(eventos);

        JScrollPane scrollPane = new JScrollPane(listaEventos);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }
}