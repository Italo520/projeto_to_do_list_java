package br.com.todolist.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tarefa extends Itens {

    private double percentual;
    private LocalDate dataConclusao;
    private int prioridade;
    private List<Subtarefa> subtarefas;

    public Tarefa(String titulo, String descricao, String criado_por, LocalDate deadline, int prioridade) {
        super(titulo, descricao, "Tarefa", criado_por, deadline);
        this.prioridade = prioridade;
        this.percentual = 0.0;
        this.dataConclusao = null;
        this.subtarefas = new ArrayList<>();
    }

    public Tarefa() {
        super();
    }

    public double getPercentual() {
        return percentual;
    }

    public void setPercentual(double percentual) {
        this.percentual = percentual;
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