// Copie e cole este código inteiro no seu arquivo Tarefa.java
package br.com.todolist.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tarefa {

    private String titulo;
    private String descricao;
    private LocalDate dataCadastro;
    private LocalDate deadline;
    private double percentual;
    private LocalDate dataConclusao; // ATRIBUTO RENOMEADO
    private int prioridade;
    private List<Tarefa> subtarefas;

    public Tarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCadastro = LocalDate.now();
        this.deadline = deadline;
        this.prioridade = prioridade;
        this.percentual = 0.0;
        this.dataConclusao = null; // ATRIBUTO RENOMEADO
        this.subtarefas = new ArrayList<>();
    }

    // --- GETTERS E SETTERS (com o nome atualizado) ---
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public double getPercentual() { return percentual; }
    public void setPercentual(double percentual) { this.percentual = percentual; }
    
    // MÉTODO GET RENOMEADO
    public LocalDate getDataConclusao() { return dataConclusao; }
    // MÉTODO SET RENOMEADO
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }

    public int getPrioridade() { return prioridade; }
    public void setPrioridade(int prioridade) { this.prioridade = prioridade; }

    public List<Tarefa> getSubtarefas() { return subtarefas; }
    public void setSubtarefas(List<Tarefa> subtarefas) { this.subtarefas = subtarefas; }
    
    @Override
    public String toString() {
        return String.format("%s (Prioridade: %d, Prazo: %s)", 
            getTitulo(), getPrioridade(), getDeadline().toString());
    }
}
