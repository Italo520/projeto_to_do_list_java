// Em: src/main/java/br/com/todolist/service/Orquestrador.java
package br.com.todolist.service;

import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.models.Usuario;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class Orquestrador {

    private final GerenteDeTarefas gerenteDeTarefas;
    private final GerenteDeEventos gerenteDeEventos;
    private final String emailUsuario;

    public Orquestrador(Usuario usuario) {
        GerenteDeDadosDoUsuario dadosDoUsuario = new GerenteDeDadosDoUsuario();
        this.emailUsuario = usuario.getEmail();
        this.gerenteDeTarefas = new GerenteDeTarefas(dadosDoUsuario, this.emailUsuario);
        this.gerenteDeEventos = new GerenteDeEventos(dadosDoUsuario, this.emailUsuario);
    }

    public void cadastrarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        Tarefa novaTarefa = new Tarefa(titulo, descricao, this.emailUsuario, deadline, prioridade);
        this.gerenteDeTarefas.cadastrarTarefa(novaTarefa);
    }

    public List<Tarefa> listarTodasTarefas() {
        return this.gerenteDeTarefas.listarTodasTarefas();
    }

    public void excluirTarefa(Tarefa tarefa) {
        this.gerenteDeTarefas.excluirTarefa(tarefa);
    }

    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) {
        this.gerenteDeTarefas.editarTarefa(tarefaOriginal, novoTitulo, novaDescricao, novoDeadline, novaPrioridade);
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
        Evento novoEvento = new Evento(titulo, descricao, this.emailUsuario, deadline);
        return this.gerenteDeEventos.cadastrarEvento(novoEvento);
    }

    public List<Evento> listarTodosEventos() {
        return this.gerenteDeEventos.listarTodosEventos();
    }

    public void excluirEvento(Evento evento) {
        this.gerenteDeEventos.excluirEvento(evento);
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        this.gerenteDeEventos.editarEvento(eventoOriginal, novoTitulo, novaDescricao, novoDeadline);
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return this.gerenteDeEventos.listarEventosPorDia(dia);
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return this.gerenteDeEventos.listarEventosPorMes(mes);
    }
}