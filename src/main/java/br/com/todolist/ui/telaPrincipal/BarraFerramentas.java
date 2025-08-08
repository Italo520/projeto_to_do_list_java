package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.service.Orquestrador;
import br.com.todolist.util.Central;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BarraFerramentas {

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATADOR_MES_ANO = DateTimeFormatter.ofPattern("MM/yyyy");

    public static JMenuBar criarBarraFerramentas(TelaPrincipal frame, Orquestrador orquestrador) {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuTarefas = new JMenu("Tarefas");
        JMenu menuEventos = new JMenu("Eventos");
        JMenu menuAjuda = new JMenu("Ajuda");

 
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });


        JMenuItem listarTarefasPorDia = new JMenuItem("Listar Tarefas por Dia");
        listarTarefasPorDia.addActionListener(new OuvinteListarTarefasPorDia(frame, orquestrador));
        
        JMenuItem listarTarefasCriticas = new JMenuItem("Listar Tarefas Críticas");
        listarTarefasCriticas.addActionListener(new OuvinteListarTarefasCriticas(frame, orquestrador));

        JMenuItem pdfDoDia = new JMenuItem("Gerar PDF das Tarefas do Dia");
        pdfDoDia.addActionListener(new OuvinteGerarPdfTarefas(frame, orquestrador));

        JMenuItem enviarEmailTarefas = new JMenuItem("Enviar Tarefas do Dia por Email");
        enviarEmailTarefas.addActionListener(new OuvinteEnviarEmailTarefas(frame, orquestrador));
        
        JMenuItem relatorioTarefasPorMes = new JMenuItem("Relatório de Tarefas por Mês (Excel)");
        relatorioTarefasPorMes.addActionListener(new OuvinteGerarExcelTarefas(frame, orquestrador));
        
        JMenuItem listarEventosPorDia = new JMenuItem("Listar Eventos por Dia");
        listarEventosPorDia.addActionListener(new OuvinteListarEventosPorDia(frame, orquestrador));
        
        JMenuItem listarEventosMesEspecifico = new JMenuItem("Listar Eventos por Mês");
        listarEventosMesEspecifico.addActionListener(new OuvinteListarEventosPorMes(frame, orquestrador));

        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Aplicação de Lista de Tarefas\nVersão 1.0\nCriado Por: Italo, Rickson e Marcus",
                "Sobre", JOptionPane.INFORMATION_MESSAGE));

        menuAjuda.add(itemSobre);

        menuEventos.add(listarEventosPorDia);
        menuEventos.add(listarEventosMesEspecifico);

        menuTarefas.add(listarTarefasPorDia);
        menuTarefas.add(listarTarefasCriticas);
        menuTarefas.addSeparator();
        menuTarefas.add(pdfDoDia);
        menuTarefas.add(enviarEmailTarefas);
        menuTarefas.add(relatorioTarefasPorMes);

        menuArquivo.add(itemSair);

        menuBar.add(menuArquivo);
        menuBar.add(menuTarefas);
        menuBar.add(menuEventos);
        menuBar.add(menuAjuda);

        return menuBar;
    }

    private static Optional<LocalDate> obterDataDoUsuario(JFrame frame, String mensagem) {
        String dataInput = JOptionPane.showInputDialog(frame, mensagem);
        if (dataInput == null || dataInput.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(dataInput, FORMATADOR_DATA));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(frame, "Formato de data inválido! Use dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    private static Optional<YearMonth> obterMesAnoDoUsuario(JFrame frame, String mensagem) {
        String mesAnoInput = JOptionPane.showInputDialog(frame, mensagem);
        if (mesAnoInput == null || mesAnoInput.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(YearMonth.parse(mesAnoInput, FORMATADOR_MES_ANO));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(frame, "Formato de data inválido! Use MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    // OUVINTES

    private static class OuvinteListarTarefasPorDia implements ActionListener {
        private final TelaPrincipal frame;
        private final Orquestrador orquestrador;

        public OuvinteListarTarefasPorDia(TelaPrincipal frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        public void actionPerformed(ActionEvent e) {
            obterDataDoUsuario(frame, "Digite a data para listar as tarefas (dd/MM/yyyy):")
                .ifPresent(dia -> {
                    List<Tarefa> tarefas = orquestrador.listarTarefasPorDia(dia);
                    if (tarefas.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Nenhuma tarefa encontrada para esta data.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    }
                    frame.atualizarPainelDeTarefas(tarefas);
                });
        }
    }

    private static class OuvinteListarTarefasCriticas implements ActionListener {
        private final TelaPrincipal frame;
        private final Orquestrador orquestrador;

        public OuvinteListarTarefasCriticas(TelaPrincipal frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        public void actionPerformed(ActionEvent e) {
            List<Tarefa> tarefas = orquestrador.listarTarefasCriticas();
            if (tarefas.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nenhuma tarefa crítica encontrada.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.atualizarPainelDeTarefas(tarefas);
        }
    }

    private static class OuvinteListarEventosPorDia implements ActionListener {
        private final TelaPrincipal frame;
        private final Orquestrador orquestrador;

        public OuvinteListarEventosPorDia(TelaPrincipal frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        public void actionPerformed(ActionEvent e) {
            obterDataDoUsuario(frame, "Digite a data para listar os eventos (dd/MM/yyyy):")
                .ifPresent(dia -> {
                    List<Evento> eventos = orquestrador.listarEventosPorDia(dia);
                     if (eventos.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Nenhum evento encontrado para esta data.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    }
                    frame.atualizarPainelDeEventos(eventos);
                });
        }
    }
    
    private static class OuvinteListarEventosPorMes implements ActionListener {
        private final TelaPrincipal frame;
        private final Orquestrador orquestrador;
        
        public OuvinteListarEventosPorMes(TelaPrincipal frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        public void actionPerformed(ActionEvent e) {
            obterMesAnoDoUsuario(frame, "Digite o mês e ano (MM/yyyy):")
                .ifPresent(mes -> {
                    List<Evento> eventos = orquestrador.listarEventosPorMes(mes);
                    if (eventos.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Nenhum evento encontrado para este mês.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    }
                    frame.atualizarPainelDeEventos(eventos);
                });
        }
    }
    

    private static class OuvinteGerarPdfTarefas implements ActionListener {
        private final JFrame frame;
        private final Orquestrador orquestrador;

        public OuvinteGerarPdfTarefas(JFrame frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        public void actionPerformed(ActionEvent e) {
            obterDataDoUsuario(frame, "Digite a data para gerar o PDF (dd/MM/yyyy):")
                .ifPresent(dia -> {
                    List<Tarefa> tarefas = orquestrador.listarTarefasPorDia(dia);
                    String[] cabecalhos = {"Título", "Descrição", "Prioridade", "Prazo"};
                    List<String[]> dados = tarefas.stream()
                        .map(t -> new String[]{t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade()), t.getDeadline().toString()})
                        .collect(Collectors.toList());
                    Central.gerarPdf("TarefasDoDia.pdf", "Relatório de Tarefas", cabecalhos, dados);
                    JOptionPane.showMessageDialog(frame, "PDF gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                });
        }
    }

    private static class OuvinteEnviarEmailTarefas implements ActionListener {
        private final JFrame frame;
        private final Orquestrador orquestrador;

        public OuvinteEnviarEmailTarefas(JFrame frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        public void actionPerformed(ActionEvent e) {
            obterDataDoUsuario(frame, "Digite a data para o envio do relatório (dd/MM/yyyy):")
                .ifPresent(dia -> {
                    boolean sucesso = orquestrador.enviarRelatorioTarefasDoDiaPorEmail(dia);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(frame, "Email com o relatório em anexo enviado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Não foi possível enviar o email.\nVerifique se existem tarefas para a data informada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                });
        }
    }

    private static class OuvinteGerarExcelTarefas implements ActionListener {
        private final JFrame frame;
        private final Orquestrador orquestrador;
        public OuvinteGerarExcelTarefas(JFrame frame, Orquestrador orquestrador) {
            this.frame = frame;
            this.orquestrador = orquestrador;
        }
        public void actionPerformed(ActionEvent e) {
            obterMesAnoDoUsuario(frame, "Digite o mês e ano para o relatório (MM/yyyy):")
                .ifPresent(mes -> {
                    String nomeArquivo = "Relatorio_Tarefas_" + mes.format(DateTimeFormatter.ofPattern("MM_yyyy")) + ".xlsx";
                    orquestrador.gerarRelatorioTarefasPorMes(mes, nomeArquivo);
                    JOptionPane.showMessageDialog(frame, "Relatório Excel '" + nomeArquivo + "' gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                });
        }
    }
}