package br.com.todolist.models;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class Tarefa extends Itens {

    private LocalDate dataConclusao;
    private int prioridade;
    private List<Subtarefa> subtarefas;

    public Tarefa() {
    }

    public Tarefa(String titulo, String descricao, String criado_por, LocalDate deadline, int prioridade) {
        super(titulo, descricao, "Tarefa", criado_por, deadline);
        this.prioridade = prioridade;
        this.dataConclusao = null;
        this.subtarefas = new ArrayList<>();
    }

    public double obterPercentual() {
        if (subtarefas.isEmpty()) {
            return dataConclusao != null ? 100.0 : 0.0;
        }

        long subtarefasConcluidas = this.subtarefas.stream()
                .filter(Subtarefa::isStatus)
                .count();

        return ((double) subtarefasConcluidas / subtarefas.size()) * 100;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public List<Subtarefa> getSubtarefas() {
        return subtarefas;
    }

    public void setSubtarefas(List<Subtarefa> subtarefas) {
        this.subtarefas = subtarefas;
    }

    public void adicionarSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.add(subtarefa);
    }

    public void removerSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.remove(subtarefa);
    }

    public String toString() {
        return getTitulo();
    }
}