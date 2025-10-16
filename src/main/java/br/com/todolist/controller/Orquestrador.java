package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.GerenciadorDePersistenciaJson;
import br.com.todolist.repository.Repositorio;
import br.com.todolist.service.Central;
import br.com.todolist.service.GeradorDeRelatorio;
import br.com.todolist.service.Mensageiro;

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
    private final GeradorDeRelatorio geradorDeRelatorio;

    public Orquestrador(
        GerenteDeTarefas gerenteDeTarefas,
        GerenteDeEventos gerenteDeEventos,
        Mensageiro mensageiro,
        GeradorDeRelatorio geradorDeRelatorio,
        Usuario usuario) {
        this.gerenteDeTarefas = gerenteDeTarefas;
        this.gerenteDeEventos = gerenteDeEventos;
        this.mensageiro = mensageiro;
        this.geradorDeRelatorio = geradorDeRelatorio;
        this.emailUsuario = usuario.getEmail();
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
        
        geradorDeRelatorio.gerarRelatorioTarefasPDF(nomeArquivo, tituloRelatorio, tarefas);

        String assunto = "Seu Relatório de Tarefas do Dia: " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String corpo = "Olá!\n\nSegue em anexo o relatório com suas tarefas para o dia " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".\n\nAtenciosamente,\nSistema ToDoList.";
        boolean sucesso = mensageiro.enviarEmailComAnexo(this.emailUsuario, assunto, corpo, nomeArquivo);

        new File(nomeArquivo).delete();

        return sucesso;
    }

    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        List<Tarefa> tarefas = this.gerenteDeTarefas.listarTodasTarefas();
        geradorDeRelatorio.gerarRelatorioTarefasExcel(nomeArquivo, mes, tarefas);
    }

}