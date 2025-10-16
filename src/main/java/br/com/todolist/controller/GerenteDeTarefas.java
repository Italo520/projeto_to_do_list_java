package br.com.todolist.controller;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.Repositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GerenteDeTarefas {

    private final Repositorio repositorio;
    private final Usuario usuario;

    public GerenteDeTarefas(Repositorio repositorio, Usuario usuario) {
        this.repositorio = repositorio;
        this.usuario = usuario;
    }

    public void cadastrarTarefa(Tarefa tarefa) {
        tarefa.setUsuario(usuario);
        usuario.getTarefas().add(tarefa);
        repositorio.salvar(usuario);
    }

    public List<Tarefa> listarTodasTarefas() {
        return usuario.getTarefas();
    }

    public void excluirTarefa(Tarefa tarefa) {
        usuario.getTarefas().remove(tarefa);
        repositorio.salvar(usuario);
    }

    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) {
        tarefaOriginal.setTitulo(novoTitulo);
        tarefaOriginal.setDescricao(novaDescricao);
        tarefaOriginal.setDeadLine(novoDeadline);
        tarefaOriginal.setPrioridade(novaPrioridade);
        repositorio.salvar(usuario);
    }

    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return usuario.getTarefas().stream()
                .filter(tarefa -> tarefa.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasCriticas() {
        LocalDate hoje = LocalDate.now();
        return usuario.getTarefas().stream()
                .filter(tarefa -> {
                    long diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(hoje, tarefa.getDeadline());
                    return (diasRestantes - tarefa.getPrioridade()) < 0;
                })
                .collect(Collectors.toList());
    }
}