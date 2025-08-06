package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.PadraoDialogo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BarraFerramentas {

    public static JMenuBar criarBarraFerramentas(JFrame frame, Orquestrador orquestrador) {

        JMenuBar menuBar = new JMenuBar();

        // Menus da Barra
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuTarefas = new JMenu("Tarefas");
        JMenu menuEventos = new JMenu("Eventos");
        JMenu menuAjuda = new JMenu("Ajuda");

        // Adiciona os menus à barra de menus principal
        menuBar.add(menuArquivo);
        menuBar.add(menuTarefas);
        menuBar.add(menuEventos);
        menuBar.add(menuAjuda);

        // --- Itens do Menu Arquivo ---

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener((ActionEvent e) -> {
            frame.dispose();
            System.exit(0);
        });

        // --- Itens do Menu Ajuda ---
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(frame,
                "Aplicação de Lista de Tarefas\nVersão 1.0",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Itens do Menu Tarefas ---
        JMenuItem listarTarefasPorDia = new JMenuItem("Listar Tarefas por Dia");
        JMenuItem listarTarefasCriticas = new JMenuItem("Listar Tarefas Criticas");
        JMenuItem pdfDoDia = new JMenuItem("PDF do Dia");
        JMenuItem enviarListaDeTarefasDoDiaPorEmail = new JMenuItem("Enviar Lista de Tarefas do Dia por Email");
        JMenuItem relatorioTarefasPorMes = new JMenuItem("Relatório de Tarefas por Mês");



        // Itens Menu Eventos

        JMenuItem listarEventosProDia = new JMenuItem("Listar Eventos por Dia");
        JMenuItem listarEventosMesEspecifico = new JMenuItem("Listar Eventos Mes Específico");



        

        listarTarefasPorDia.addActionListener((ActionEvent e) -> {
    Optional<LocalDate> dataOpcional = PadraoDialogo.pedirData(
            frame,
            "Listar Tarefas por Dia",
            "Digite a data (formato dd/MM/yyyy):"
    );

    // O ifPresent garante que o código só execute se o usuário fornecer uma data
    dataOpcional.ifPresent(data -> {
        // 1. Busca os dados no orquestrador
        List<Tarefa> tarefas = orquestrador.listarTarefasPorDia(data);

        // 2. Manda a TelaPrincipal atualizar o painel com esses dados
        if (frame instanceof TelaPrincipal) {
            ((TelaPrincipal) frame).atualizarPainelDeTarefas(tarefas);
        }

        // 3. Mostra uma mensagem informativa se a lista estiver vazia
        if (tarefas.isEmpty()) {
            PadraoDialogo.mostrarMensagemInfo(frame, "Nenhuma tarefa encontrada para este dia.");
        }
    });
});
           

        // Adiciona os itens aos seus respectivos menus
        menuArquivo.add(itemSair);
        menuAjuda.add(itemSobre);
        menuTarefas.add(listarTarefasPorDia);
        menuTarefas.add(listarTarefasCriticas);
        menuTarefas.add(pdfDoDia);
        menuTarefas.add(enviarListaDeTarefasDoDiaPorEmail);
        menuTarefas.add(relatorioTarefasPorMes);
        menuEventos.add(listarEventosProDia);
        menuEventos.add(listarEventosMesEspecifico);

        return menuBar;
    }
}