// Substitua o conteúdo em: src/main/java/br/com/todolist/models/Tarefa.java

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
    private LocalDate dataConclusao;
    private int prioridade;
    private List<Subtarefa> subtarefas; // <-- Alterado para usar a classe Subtarefa

    public Tarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCadastro = LocalDate.now();
        this.deadline = deadline;
        this.prioridade = prioridade;
        this.percentual = 0.0;
        this.dataConclusao = null;
        this.subtarefas = new ArrayList<>(); // <-- Inicializa a lista correta
    }

    // --- GETTERS E SETTERS ---
    
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
    
    public LocalDate getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }

    public int getPrioridade() { return prioridade; }
    public void setPrioridade(int prioridade) { this.prioridade = prioridade; }

    // --- MÉTODOS PARA SUBTAREFAS (AGORA CORRETOS) ---
    
    public List<Subtarefa> getSubtarefas() { return subtarefas; }
    public void setSubtarefas(List<Subtarefa> subtarefas) { this.subtarefas = subtarefas; }

    /**
     * Adiciona uma subtarefa à lista de subtarefas da tarefa.
     * @param subtarefa A subtarefa a ser adicionada.
     */
    public void adicionarSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.add(subtarefa); // <-- IMPLEMENTADO
    }

    /**
     * Remove uma subtarefa da lista de subtarefas da tarefa.
     * @param subtarefa A subtarefa a ser removida.
     */
    public void removerSubtarefa(Subtarefa subtarefa) {
        this.subtarefas.remove(subtarefa); // <-- IMPLEMENTADO
    }
    
    @Override
    public String toString() {
        return getTitulo(); // Alterado para exibir apenas o título na JTree
    }
}