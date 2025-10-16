package br.com.todolist.service;

import br.com.todolist.entity.Tarefa;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GeradorDeRelatorio {

    public void gerarRelatorioTarefasPDF(String nomeArquivo, String tituloRelatorio, List<Tarefa> tarefas) {
        String[] cabecalhos = {"Título", "Descrição", "Prioridade"};
        List<String[]> dados = tarefas.stream()
            .map(t -> new String[]{t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade())})
            .collect(Collectors.toList());

        Central.gerarPdf(nomeArquivo, tituloRelatorio, cabecalhos, dados);
    }

    public void gerarRelatorioTarefasExcel(String nomeArquivo, YearMonth mes, List<Tarefa> tarefas) {
        List<Tarefa> tarefasDoMes = tarefas.stream()
            .filter(t -> YearMonth.from(t.getDeadline()).equals(mes))
            .collect(Collectors.toList());

        String[] cabecalhos = {"Título", "Descrição", "Prioridade", "Prazo", "Conclusão (%)"};

        List<String[]> dados = tarefasDoMes.stream()
            .map(t -> new String[]{
                t.getTitulo(),
                t.getDescricao(),
                String.valueOf(t.getPrioridade()),
                t.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            })
            .collect(Collectors.toList());

        List<String> colunaExtra = tarefasDoMes.stream()
            .map(t -> String.format("%.0f%%", t.obterPercentual()))
            .collect(Collectors.toList());

        String nomePlanilha = "Tarefas de " + mes.format(DateTimeFormatter.ofPattern("MM-yyyy"));
        Central.gerarExcel(nomeArquivo, nomePlanilha, cabecalhos, dados, colunaExtra);
    }
}