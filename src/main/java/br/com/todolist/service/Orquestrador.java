package br.com.todolist.service;

import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.models.Usuario;
import br.com.todolist.util.Mensageiro;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

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
        String assunto = "Notificação: Nova Tarefa Criada";
        String corpo = "Uma nova tarefa foi criada:\n\n"
                + "Título: " + titulo + "\n"
                + "Descrição: " + descricao + "\n"
                + "Prazo: " + deadline + "\n";
        mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
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
        if (sucesso) {
            String assunto = "Notificação: Novo Evento Criado";
            String corpo = "Um novo evento foi criado:\n\n"
                    + "Título: " + titulo + "\n"
                    + "Descrição: " + descricao + "\n"
                    + "Prazo: " + deadline + "\n";
            mensageiro.enviarEmail(this.emailUsuario, assunto, corpo);
        }
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
}