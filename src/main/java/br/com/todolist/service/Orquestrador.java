package br.com.todolist.service;

import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.models.Usuario;
import br.com.todolist.util.Central;
import br.com.todolist.util.Mensageiro;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Orquestrador {

    private final GerenteDeTarefas gerenteDeTarefas;
    private final GerenteDeEventos gerenteDeEventos;
    private final String emailUsuario;
    private final Mensageiro mensageiro;

    public Orquestrador(Usuario usuario) {
        GerenteDeDadosDoUsuario dadosDoUsuario = new GerenteDeDadosDoUsuario();
        this.emailUsuario = usuario.getEmail();
        this.gerenteDeTarefas = new GerenteDeTarefas(dadosDoUsuario, this.emailUsuario);
        this.gerenteDeEventos = new GerenteDeEventos(dadosDoUsuario, this.emailUsuario);
        this.mensageiro = new Mensageiro();
    }

    public void cadastrarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        Tarefa novaTarefa = new Tarefa(titulo, descricao, this.emailUsuario, deadline, prioridade);
        this.gerenteDeTarefas.cadastrarTarefa(novaTarefa);
        // String assunto = "Notificação: Nova Tarefa Criada";
        // String corpo = "Uma nova tarefa foi criada:\n\n"
        // + "Título: " + titulo + "\n"
        // + "Descrição: " + descricao + "\n"
        // + "Prazo: " + deadline + "\n";
        // mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }

    public List<Tarefa> listarTodasTarefas() {
        return this.gerenteDeTarefas.listarTodasTarefas();
    }

    public void excluirTarefa(Tarefa tarefa) {
        this.gerenteDeTarefas.excluirTarefa(tarefa);
        String assunto = "Notificação: Tarefa Excluída";
        String corpo = "A tarefa '" + tarefa.getTitulo() + "' foi excluída.";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }

    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) {
        this.gerenteDeTarefas.editarTarefa(tarefaOriginal, novoTitulo, novaDescricao, novoDeadline, novaPrioridade);
        String assunto = "Notificação: Tarefa Editada";
        String corpo = "A tarefa '" + tarefaOriginal.getTitulo() + "' foi editada.";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }

    public void atualizarTarefa(Tarefa tarefa) {
        this.gerenteDeTarefas.atualizarTarefa(tarefa);
    }

    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return this.gerenteDeTarefas.listarTarefasPorDia(dia);
    }

    public List<Tarefa> listarTarefasCriticas() {
        return this.gerenteDeTarefas.listarTarefasCriticas();
    }

    public boolean cadastrarEvento(String titulo, String descricao, LocalDate deadline) {
        boolean sucesso = this.gerenteDeEventos
                .cadastrarEvento(new Evento(titulo, descricao, this.emailUsuario, deadline));
        // if (sucesso) {
        // String assunto = "Notificação: Novo Evento Criado";
        // String corpo = "Um novo evento foi criado:\n\n"
        // + "Título: " + titulo + "\n"
        // + "Descrição: " + descricao + "\n"
        // + "Prazo: " + deadline + "\n";
        // mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
        // }
        return sucesso;
    }

    public List<Evento> listarTodosEventos() {
        return this.gerenteDeEventos.listarTodosEventos();
    }

    public void excluirEvento(Evento evento) {
        this.gerenteDeEventos.excluirEvento(evento);
        String assunto = "Notificação: Evento Excluído";
        String corpo = "O evento '" + evento.getTitulo() + "' foi excluído.";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        this.gerenteDeEventos.editarEvento(eventoOriginal, novoTitulo, novaDescricao, novoDeadline);
        String assunto = "Notificação: Evento Editado";
        String corpo = "O evento '" + eventoOriginal.getTitulo() + "' foi editado.";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return this.gerenteDeEventos.listarEventosPorDia(dia);
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return this.gerenteDeEventos.listarEventosPorMes(mes);
    }

    public boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia) {
        List<Tarefa> tarefas = this.gerenteDeTarefas.listarTarefasPorDia(dia);
        if (tarefas == null || tarefas.isEmpty()) {
            return false; 
        }

        String nomeArquivo = "Relatorio_Tarefas_" + dia.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".pdf";
        String tituloRelatorio = "Relatório de Tarefas - " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        String[] cabecalhos = {"Título", "Descrição", "Prioridade"};
        List<String[]> dados = tarefas.stream()
            .map(t -> new String[]{t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade())})
            .collect(Collectors.toList());

        Central.gerarPdf(nomeArquivo, tituloRelatorio, cabecalhos, dados);

        String assunto = "Seu Relatório de Tarefas do Dia: " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String corpo = "Olá!\n\nSegue em anexo o relatório com suas tarefas para o dia " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".\n\nAtenciosamente,\nSistema ToDoList.";
        boolean sucesso = mensageiro.enviarEmailComAnexo(this.emailUsuario, assunto, corpo, nomeArquivo);

        new File(nomeArquivo).delete();

        return sucesso;
    }

    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
    List<Tarefa> tarefasDoMes = this.gerenteDeTarefas.listarTodasTarefas().stream()
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