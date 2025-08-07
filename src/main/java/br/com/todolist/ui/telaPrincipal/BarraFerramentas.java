// Em: src/main/java/br/com/todolist/ui/telaPrincipal/BarraFerramentas.java
package br.com.todolist.ui.telaPrincipal;



import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.PadraoDialogo;
import br.com.todolist.util.Central;

import javax.swing.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class BarraFerramentas {

    public static JMenuBar criarBarraFerramentas(JFrame frame, Orquestrador orquestrador) {
        // 1. Crie a barra de menus principal
        JMenuBar menuBar = new JMenuBar();

        // 2. Crie os menus que irão na barra
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuTarefas = new JMenu("Tarefas");
        JMenu menuEventos = new JMenu("Eventos");
        JMenu menuAjuda = new JMenu("Ajuda");
        

        // --- Itens do Menu Arquivo ---
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });

        // --- Itens do Menu Tarefas ---
        JMenuItem listarTarefasPorDia = new JMenuItem("Listar Tarefas por Dia");
        listarTarefasPorDia.addActionListener(e -> {
            String dataInput = JOptionPane.showInputDialog(frame, "Digite a data (dd/MM/yyyy):");
            try {
                LocalDate dia = LocalDate.parse(dataInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                List<Tarefa> tarefas = orquestrador.listarTarefasPorDia(dia);
                exibirResultados(frame, "Tarefas do dia " + dataInput, tarefas);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem listarTarefasCriticas = new JMenuItem("Listar Tarefas Críticas");
        listarTarefasCriticas.addActionListener(e -> {
            List<Tarefa> tarefas = orquestrador.listarTarefasCriticas();
            exibirResultados(frame, "Tarefas Críticas", tarefas);
        });

        JMenuItem pdfDoDia = new JMenuItem("Gerar PDF das Tarefas do Dia");
        pdfDoDia.addActionListener(e -> {
            String dataInput = JOptionPane.showInputDialog(frame, "Digite a data (dd/MM/yyyy):");
             try {
                LocalDate dia = LocalDate.parse(dataInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                List<Tarefa> tarefas = orquestrador.listarTarefasPorDia(dia);
                
                String[] cabecalhos = {"Título", "Descrição", "Prioridade", "Prazo"};
                List<String[]> dados = tarefas.stream()
                    .map(t -> new String[]{t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade()), t.getDeadline().toString()})
                    .collect(Collectors.toList());
                
                Central.gerarPdf("TarefasDoDia.pdf", "Relatório de Tarefas", cabecalhos, dados);
                JOptionPane.showMessageDialog(frame, "PDF gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem enviarListaDeTarefasDoDiaPorEmail = new JMenuItem("Enviar Tarefas do Dia por Email");
        enviarListaDeTarefasDoDiaPorEmail.addActionListener(e -> {
            // Este método não existe no seu Orquestrador, mas seria algo assim:
            // orquestrador.enviarTarefasPorEmail();
            JOptionPane.showMessageDialog(frame, "Funcionalidade ainda não implementada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JMenuItem relatorioTarefasPorMes = new JMenuItem("Relatório de Tarefas por Mês (Excel)");
        relatorioTarefasPorMes.addActionListener(e -> {
            // Este método também não existe, mas a lógica seria similar:
            JOptionPane.showMessageDialog(frame, "Funcionalidade ainda não implementada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- Itens do Menu Eventos ---
        JMenuItem listarEventosPorDia = new JMenuItem("Listar Eventos por Dia");
        listarEventosPorDia.addActionListener(e -> {
             String dataInput = JOptionPane.showInputDialog(frame, "Digite a data (dd/MM/yyyy):");
            try {
                LocalDate dia = LocalDate.parse(dataInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                List<Evento> eventos = orquestrador.listarEventosPorDia(dia);
                exibirResultados(frame, "Eventos do dia " + dataInput, eventos);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JMenuItem listarEventosMesEspecifico = new JMenuItem("Listar Eventos por Mês");
        listarEventosMesEspecifico.addActionListener(e -> {
            String mesAnoInput = JOptionPane.showInputDialog(frame, "Digite o mês e ano (MM/yyyy):");
            try {
                YearMonth mes = YearMonth.parse(mesAnoInput, DateTimeFormatter.ofPattern("MM/yyyy"));
                List<Evento> eventos = orquestrador.listarEventosPorMes(mes);
                exibirResultados(frame, "Eventos de " + mesAnoInput, eventos);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Itens do Menu Ajuda ---
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                "Aplicação de Lista de Tarefas\nVersão 1.0",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE);
        });

        // 4. Adicione os itens aos seus respectivos menus
        menuArquivo.add(itemSair);
        
        menuTarefas.add(listarTarefasPorDia);
        menuTarefas.add(listarTarefasCriticas);
        menuTarefas.addSeparator(); // Linha separadora
        menuTarefas.add(pdfDoDia);
        menuTarefas.add(enviarListaDeTarefasDoDiaPorEmail);
        menuTarefas.add(relatorioTarefasPorMes);
        
        menuEventos.add(listarEventosPorDia);
        menuEventos.add(listarEventosMesEspecifico);
        
        menuAjuda.add(itemSobre);

        // 5. Adicione os menus à barra de menus principal
        menuBar.add(menuArquivo);
        menuBar.add(menuTarefas);
        menuBar.add(menuEventos);
        menuBar.add(menuAjuda);

        return menuBar;
    }

    /**
     * Método auxiliar para exibir uma lista de resultados em um JOptionPane.
     */
    private static void exibirResultados(JFrame frame, String titulo, List<?> resultados) {
    // 1. Caso a lista esteja vazia, usa o método padrão de informação.
    if (resultados == null || resultados.isEmpty()) {
        PadraoDialogo.mostrarMensagemInfo(frame, "Nenhum item encontrado.");
        return;
    }
    
    // 2. Constrói a string com todos os resultados.
    // A lógica de preparar o conteúdo continua aqui.
    StringBuilder conteudo = new StringBuilder();
    for (Object item : resultados) {
        conteudo.append(item.toString()).append("\n");
    }
    
    // 3. Usa o método padrão para exibir resultados, que já cria o JTextArea e o JScrollPane.
    PadraoDialogo.mostrarResultados(frame, titulo, conteudo.toString());
}
}