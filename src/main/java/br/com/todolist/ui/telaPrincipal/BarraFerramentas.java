// Em: src/main/java/br/com/todolist/ui/telaPrincipal/BarraFerramentas.java
package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.ui.TelasDialogo.PadraoDialogo; // Supondo que você tem esta classe para diálogos
import br.com.todolist.util.Central;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BarraFerramentas {

    // Assinatura corrigida para receber o Orquestrador
    public static JMenuBar criarBarraFerramentas(JFrame frame, Orquestrador orquestrador) {

        JMenuBar menuBar = new JMenuBar();
        // ... (criação dos menus permanece a mesma)
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuTarefas = new JMenu("Tarefas");
        JMenu menuEventos = new JMenu("Eventos");
        JMenu menuAjuda = new JMenu("Ajuda");

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> PadraoDialogo.mostrarMensagemInfo(frame, "Aplicação de Lista de Tarefas\nVersão 1.0"));

        // --- Itens do Menu com nomes padronizados ---
        JMenuItem listarTarefasPorDia = new JMenuItem("Listar Tarefas por Dia");
        JMenuItem mostrarTodasTarefas = new JMenuItem("Mostrar Todas as Tarefas");
        JMenuItem listarTarefasCriticas = new JMenuItem("Listar Tarefas Críticas");
        JMenuItem gerarPdfDoDia = new JMenuItem("Gerar PDF de Tarefas do Dia");
        
        JMenuItem listarEventosPorDia = new JMenuItem("Listar Eventos por Dia");
        JMenuItem listarEventosPorMes = new JMenuItem("Listar Eventos por Mês");

        // --- AÇÕES DO MENU TAREFAS (CORRIGIDAS) ---

        // Ação para "Listar Tarefas por Dia"
        listarTarefasPorDia.addActionListener(e -> {
            Optional<LocalDate> dataOpcional = PadraoDialogo.pedirData(frame, "Listar Tarefas por Dia", "Digite a data (formato dd/MM/yyyy):");
            dataOpcional.ifPresent(data -> {
                if (frame instanceof TelaPrincipal) {
                    // CORREÇÃO: Chamando o método padronizado da TelaPrincipal
                    ((TelaPrincipal) frame).listarTarefasPorDia(data);
                }
            });
        });
        
        // Ação para "Mostrar Todas as Tarefas"
        mostrarTodasTarefas.addActionListener(e -> {
             if (frame instanceof TelaPrincipal) {
                // CORREÇÃO: Chamando o método padronizado da TelaPrincipal
                ((TelaPrincipal) frame).mostrarTodasAsTarefas();
            }
        });

        // Ação para "Listar Tarefas Críticas" (lógica mantida pois não atualiza o painel principal)
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

        // Ação para "Gerar PDF de Tarefas do Dia" (lógica mantida)
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
                    // Supondo que orquestrador não tenha mais o método obterPercentual, pegamos direto da tarefa
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
        
        // ... (Ações do menu de eventos e montagem final permanecem as mesmas) ...
        
        menuArquivo.add(itemSair);
        menuAjuda.add(itemSobre);
        
        menuTarefas.add(listarTarefasPorDia);
        menuTarefas.add(mostrarTodasTarefas);
        menuTarefas.addSeparator();
        menuTarefas.add(listarTarefasCriticas);
        menuTarefas.add(gerarPdfDoDia);

        menuEventos.add(listarEventosPorDia);
        menuEventos.add(listarEventosPorMes);

        menuBar.add(menuArquivo);
        menuBar.add(menuTarefas);
        menuBar.add(menuEventos);
        menuBar.add(menuAjuda);

        return menuBar;
    }
}