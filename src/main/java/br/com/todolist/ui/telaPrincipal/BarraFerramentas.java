// Em: src/main/java/br/com/todolist/ui/telaPrincipal/BarraFerramentas.java
package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.PadraoDialogo;
import br.com.todolist.util.Central;

import javax.swing.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BarraFerramentas {

    public static JMenuBar criarBarraFerramentas(JFrame frame, Orquestrador orquestrador) {

        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuTarefas = new JMenu("Tarefas");
        JMenu menuEventos = new JMenu("Eventos");
        JMenu menuAjuda = new JMenu("Ajuda");

        // --- Itens do Menu Eventos ---
        JMenuItem listarEventosPorDia = new JMenuItem("Listar Eventos por Dia");
        JMenuItem listarEventosPorMes = new JMenuItem("Listar Eventos por Mês");
        JMenuItem mostrarTodosEventos = new JMenuItem("Mostrar Todos os Eventos");

        // --- AÇÕES DO MENU EVENTOS (REFATORADAS) ---

        // Ação para "Listar Eventos por Dia"
        listarEventosPorDia.addActionListener(e -> {
            Optional<LocalDate> dataOpcional = PadraoDialogo.pedirData(frame, "Listar Eventos por Dia", "Digite a data (formato dd/MM/yyyy):");
            dataOpcional.ifPresent(data -> {
                if (frame instanceof TelaPrincipal) {
                    ((TelaPrincipal) frame).listarEventosPorDia(data);
                }
            });
        });

        // Ação para "Listar Eventos por Mês"
        listarEventosPorMes.addActionListener(e -> {
            Optional<String> mesAnoOpcional = PadraoDialogo.pedirTexto(frame, "Listar Eventos por Mês", "Digite o mês e ano (MM/yyyy):");
            mesAnoOpcional.ifPresent(mesAnoInput -> {
                try {
                    YearMonth yearMonth = YearMonth.parse(mesAnoInput, DateTimeFormatter.ofPattern("MM/yyyy"));
                    if (frame instanceof TelaPrincipal) {
                        ((TelaPrincipal) frame).listarEventosPorMes(yearMonth);
                    }
                } catch (DateTimeParseException ex) {
                    PadraoDialogo.mostrarMensagemErro(frame, "Formato de data inválido. Use MM/yyyy.");
                }
            });
        });

        // Ação para "Mostrar Todos os Eventos"
        mostrarTodosEventos.addActionListener(e -> {
            if (frame instanceof TelaPrincipal) {
                ((TelaPrincipal) frame).mostrarTodosOsEventos();
            }
        });

        // --- Montagem final dos Menus ---
        // ... (o restante da classe permanece o mesmo)
        menuEventos.add(listarEventosPorDia);
        menuEventos.add(listarEventosPorMes);
        menuEventos.addSeparator();
        menuEventos.add(mostrarTodosEventos);

        menuBar.add(menuArquivo);
        menuBar.add(menuTarefas);
        menuBar.add(menuEventos);
        menuBar.add(menuAjuda);

        // --- Itens do Menu Arquivo ---
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> PadraoDialogo.mostrarMensagemInfo(frame, "Aplicação de Lista de Tarefas\nVersão 1.0"));

        // --- Itens do Menu Tarefas ---
        JMenuItem listarTarefasPorDia = new JMenuItem("Listar Tarefas por Dia");
        JMenuItem mostrarTodasTarefas = new JMenuItem("Mostrar Todas as Tarefas");
        JMenuItem listarTarefasCriticas = new JMenuItem("Listar Tarefas Críticas");
        JMenuItem gerarPdfDoDia = new JMenuItem("Gerar PDF de Tarefas do Dia");

        listarTarefasPorDia.addActionListener(e -> {
            Optional<LocalDate> dataOpcional = PadraoDialogo.pedirData(frame, "Listar Tarefas por Dia", "Digite a data (formato dd/MM/yyyy):");
            dataOpcional.ifPresent(data -> {
                if (frame instanceof TelaPrincipal) {
                    ((TelaPrincipal) frame).listarTarefasPorDia(data);
                }
            });
        });
        
        mostrarTodasTarefas.addActionListener(e -> {
             if (frame instanceof TelaPrincipal) {
                ((TelaPrincipal) frame).mostrarTodasAsTarefas();
            }
        });

        listarTarefasCriticas.addActionListener(e -> {
            List<Tarefa> tarefas = orquestrador.listarTarefasCriticas();
            if (tarefas.isEmpty()) {
                PadraoDialogo.mostrarMensagemInfo(frame, "Nenhuma tarefa crítica encontrada.");
            } else {
                StringBuilder sb = new StringBuilder("Tarefas Críticas (Prazo apertado para a prioridade):\n\n");
                tarefas.forEach(t -> sb.append(String.format("- %s (Prazo: %s, Prioridade: %d)\n", t.getTitulo(), t.getDeadline(), t.getPrioridade())));
                PadraoDialogo.mostrarResultados(frame, "Tarefas Críticas", sb.toString());
            }
        });

        gerarPdfDoDia.addActionListener(e -> {
            Optional<LocalDate> dataOpcional = PadraoDialogo.pedirData(frame, "Gerar PDF", "Gerar relatório PDF das tarefas de qual dia? (dd/MM/yyyy)");
            dataOpcional.ifPresent(data -> {
                List<Tarefa> tarefas = orquestrador.listarTarefasPorDia(data);
                if (tarefas.isEmpty()) {
                    PadraoDialogo.mostrarMensagemInfo(frame, "Nenhuma tarefa encontrada neste dia para gerar o PDF.");
                    return;
                }
                
                String nomeArquivo = "Relatorio_Tarefas_" + data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".pdf";
                String titulo = "Relatório de Tarefas - " + data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String[] cabecalhos = {"Título", "Descrição", "Prioridade", "Progresso"};
                
                List<String[]> dados = new ArrayList<>();
                for(Tarefa t : tarefas) {
                    dados.add(new String[]{
                        t.getTitulo(),
                        t.getDescricao(),
                        String.valueOf(t.getPrioridade()),
                        String.format("%.0f%%", orquestrador.obterPercentual(t))
                    });
                }
                
                Central.gerarPdf(nomeArquivo, titulo, cabecalhos, dados);
                PadraoDialogo.mostrarMensagemInfo(frame, "PDF gerado com sucesso!\nArquivo: " + nomeArquivo);
            });
        });
        
        menuArquivo.add(itemSair);
        menuAjuda.add(itemSobre);
        
        menuTarefas.add(listarTarefasPorDia);
        menuTarefas.add(mostrarTodasTarefas);
        menuTarefas.addSeparator();
        menuTarefas.add(listarTarefasCriticas);
        menuTarefas.add(gerarPdfDoDia);

        return menuBar;
    }
}