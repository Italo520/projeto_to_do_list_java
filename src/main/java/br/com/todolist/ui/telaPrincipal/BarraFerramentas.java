// Em: src/main/java/br/com/todolist/ui/telaPrincipal/BarraFerramentas.java
package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.util.Central;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class BarraFerramentas {

    public static JMenuBar criarBarraFerramentas(JFrame frame, Orquestrador orquestrador) {
        JMenuBar menuBar = new JMenuBar();

        // Menus
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuTarefas = new JMenu("Tarefas");
        JMenu menuEventos = new JMenu("Eventos");
        JMenu menuAjuda = new JMenu("Ajuda");

        // Itens dos Menus
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(new OuvinteBotaoSair(frame));

        JMenuItem listarTarefasPorDia = new JMenuItem("Listar Tarefas por Dia");
        listarTarefasPorDia.addActionListener(new OuvinteBotaoListarTarefasPorDia(frame, orquestrador));

        JMenuItem listarTarefasCriticas = new JMenuItem("Listar Tarefas Críticas");
        listarTarefasCriticas.addActionListener(new OuvinteBotaoListarTarefasCriticas(frame, orquestrador));

        JMenuItem pdfDoDia = new JMenuItem("Gerar PDF das Tarefas do Dia");
        pdfDoDia.addActionListener(new OuvinteBotaoGerarPdfTarefas(frame, orquestrador));

        JMenuItem enviarListaDeTarefasDoDiaPorEmail = new JMenuItem("Enviar Tarefas do Dia por Email");
        enviarListaDeTarefasDoDiaPorEmail.addActionListener(new OuvinteBotaoEnviarEmailTarefas(frame, orquestrador));
        
        JMenuItem relatorioTarefasPorMes = new JMenuItem("Relatório de Tarefas por Mês (Excel)");
        relatorioTarefasPorMes.addActionListener(new OuvinteBotaoGerarExcelTarefas(frame, orquestrador));

        JMenuItem listarEventosPorDia = new JMenuItem("Listar Eventos por Dia");
        listarEventosPorDia.addActionListener(new OuvinteBotaoListarEventosPorDia(frame, orquestrador));
        
        JMenuItem listarEventosMesEspecifico = new JMenuItem("Listar Eventos por Mês");
        listarEventosMesEspecifico.addActionListener(new OuvinteBotaoListarEventosPorMes(frame, orquestrador));

        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(new OuvinteBotaoSobre(frame));

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

    private static void exibirResultados(JFrame frame, String titulo, List<?> resultados) {
        if (resultados == null || resultados.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nenhum item encontrado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder conteudo = new StringBuilder();
        for (Object item : resultados) {
            conteudo.append(item.toString()).append("\n");
        }
        
        JTextArea areaDeTexto = new JTextArea(conteudo.toString());
        areaDeTexto.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaDeTexto);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        JOptionPane.showMessageDialog(frame, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }



    // OUVINTES
    private static class OuvinteBotaoSair implements ActionListener {
        private final JFrame frame;
        public OuvinteBotaoSair(JFrame frame) { this.frame = frame; }
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            System.exit(0);
        }
    }

    private static class OuvinteBotaoListarTarefasPorDia implements ActionListener {
        private final JFrame frame;
        private final Orquestrador orquestrador;
        public OuvinteBotaoListarTarefasPorDia(JFrame frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String dataInput = JOptionPane.showInputDialog(frame, "Digite a data (dd/MM/yyyy):");
            try {
                if (dataInput != null && !dataInput.trim().isEmpty()) {
                    LocalDate dia = LocalDate.parse(dataInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    List<Tarefa> tarefas = orquestrador.listarTarefasPorDia(dia);
                    exibirResultados(frame, "Tarefas do dia " + dataInput, tarefas);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static class OuvinteBotaoListarTarefasCriticas implements ActionListener {
        private final JFrame frame;
        private final Orquestrador orquestrador;
        public OuvinteBotaoListarTarefasCriticas(JFrame frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }

        public void actionPerformed(ActionEvent e) {
            List<Tarefa> tarefas = orquestrador.listarTarefasCriticas();
            exibirResultados(frame, "Tarefas Críticas", tarefas);
        }
    }


    private static class OuvinteBotaoGerarPdfTarefas implements ActionListener {
        private final JFrame frame;
        private final Orquestrador orquestrador;
        public OuvinteBotaoGerarPdfTarefas(JFrame frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String dataInput = JOptionPane.showInputDialog(frame, "Digite a data (dd/MM/yyyy):");
            try {
                if (dataInput != null && !dataInput.trim().isEmpty()) {
                    LocalDate dia = LocalDate.parse(dataInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    List<Tarefa> tarefas = orquestrador.listarTarefasPorDia(dia);
                    String[] cabecalhos = {"Título", "Descrição", "Prioridade", "Prazo"};
                    List<String[]> dados = tarefas.stream()
                        .map(t -> new String[]{t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade()), t.getDeadline().toString()})
                        .collect(Collectors.toList());
                    Central.gerarPdf("TarefasDoDia.pdf", "Relatório de Tarefas", cabecalhos, dados);
                    JOptionPane.showMessageDialog(frame, "PDF gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class OuvinteBotaoListarEventosPorDia implements ActionListener {
        private final JFrame frame;
        private final Orquestrador orquestrador;
        public OuvinteBotaoListarEventosPorDia(JFrame frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String dataInput = JOptionPane.showInputDialog(frame, "Digite a data (dd/MM/yyyy):");
            try {
                if (dataInput != null && !dataInput.trim().isEmpty()) {
                    LocalDate dia = LocalDate.parse(dataInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    List<Evento> eventos = orquestrador.listarEventosPorDia(dia);
                    exibirResultados(frame, "Eventos do dia " + dataInput, eventos);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    private static class OuvinteBotaoListarEventosPorMes implements ActionListener {
        private final JFrame frame;
        private final Orquestrador orquestrador;
        public OuvinteBotaoListarEventosPorMes(JFrame frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        public void actionPerformed(ActionEvent e) {
            String mesAnoInput = JOptionPane.showInputDialog(frame, "Digite o mês e ano (MM/yyyy):");
            try {
                if (mesAnoInput != null && !mesAnoInput.trim().isEmpty()) {
                    YearMonth mes = YearMonth.parse(mesAnoInput, DateTimeFormatter.ofPattern("MM/yyyy"));
                    List<Evento> eventos = orquestrador.listarEventosPorMes(mes);
                    exibirResultados(frame, "Eventos de " + mesAnoInput, eventos);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static class OuvinteBotaoSobre implements ActionListener {
        private final JFrame frame;
        public OuvinteBotaoSobre(JFrame frame) { this.frame = frame; }

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame,
                "Aplicação de Lista de Tarefas\nVersão 1.0\nCriado Por: Italo, Rickson e Marcus",
                "Sobre",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private static class OuvinteBotaoEnviarEmailTarefas implements ActionListener {
        private final JFrame frame;
        private final Orquestrador orquestrador;

        public OuvinteBotaoEnviarEmailTarefas(JFrame frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String dataInput = JOptionPane.showInputDialog(frame, "Digite a data (dd/MM/yyyy) para o envio do relatório:");
            try {
                if (dataInput != null && !dataInput.trim().isEmpty()) {
                    LocalDate dia = LocalDate.parse(dataInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    boolean sucesso = orquestrador.enviarRelatorioTarefasDoDiaPorEmail(dia);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(frame, "Email com o relatório em anexo enviado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Não foi possível enviar o email.\nVerifique se existem tarefas para a data informada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class OuvinteBotaoGerarExcelTarefas implements ActionListener {
        private final JFrame frame;
        private final Orquestrador orquestrador;

        public OuvinteBotaoGerarExcelTarefas(JFrame frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String mesAnoInput = JOptionPane.showInputDialog(frame, "Digite o mês e ano (MM/yyyy) para o relatório:");
            try {
                if (mesAnoInput != null && !mesAnoInput.trim().isEmpty()) {
                    YearMonth mes = YearMonth.parse(mesAnoInput, DateTimeFormatter.ofPattern("MM/yyyy"));
                    String nomeArquivo = "Relatorio_Tarefas_" + mes.getMonthValue() + "_" + mes.getYear() + ".xlsx";
                    orquestrador.gerarRelatorioTarefasPorMes(mes, nomeArquivo);
                    JOptionPane.showMessageDialog(frame, "Relatório Excel '" + nomeArquivo + "' gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}