package br.com.todolist.service;

import br.com.todolist.models.Tarefa;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


public class GerenteDeTarefas {

    private final GerenteDeDadosDoUsuario gerenciadorDeDados;

    public GerenteDeTarefas(GerenteDeDadosDoUsuario gerenciadorDeDados) {
        this.gerenciadorDeDados = gerenciadorDeDados;
    }

    public void cadastrarTarefa(Tarefa tarefa) {
        gerenciadorDeDados.getTarefas().add(tarefa);
        gerenciadorDeDados.salvarDados();
    }

    public List<Tarefa> listarTodasTarefas() {
        return gerenciadorDeDados.getTarefas();
    }

    public void excluirTarefa(Tarefa tarefa) {
        gerenciadorDeDados.getTarefas().remove(tarefa);
        gerenciadorDeDados.salvarDados();
    }
    
    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade) {
        tarefaOriginal.setTitulo(novoTitulo);
        tarefaOriginal.setDescricao(novaDescricao);
        tarefaOriginal.setDeadline(novoDeadline);
        tarefaOriginal.setPrioridade(novaPrioridade);
        gerenciadorDeDados.salvarDados();
    }

    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return gerenciadorDeDados.getTarefas().stream()
                .filter(tarefa -> tarefa.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasCriticas() {
        LocalDate hoje = LocalDate.now();
        return gerenciadorDeDados.getTarefas().stream()
                .filter(tarefa -> {
                    long diasRestantes = ChronoUnit.DAYS.between(hoje, tarefa.getDeadline());
                    return (diasRestantes - tarefa.getPrioridade()) < 0;
                })
                .collect(Collectors.toList());
    }
}