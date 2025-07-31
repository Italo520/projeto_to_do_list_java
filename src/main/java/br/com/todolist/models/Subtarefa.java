// Copie e cole este código inteiro no seu arquivo Subtarefa.java

package br.com.poo.todolist.models;

public class Subtarefa {

    private String titulo;
    // O método mapToInt espera um 'int', então este atributo deve ser int.
    private int percentualConcluido; 

    public Subtarefa(String titulo) {
        this.titulo = titulo;
        this.percentualConcluido = 0;
    }

    // --- GETTERS E SETTERS ---

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Este é o método que o 'mapToInt' precisa encontrar.
     * Ele não recebe parâmetros e retorna um int.
     */
    public int getPercentualConcluido() {
        return percentualConcluido;
    }

    public void setPercentualConcluido(int percentualConcluido) {
        // Garante que o percentual fique entre 0 e 100
        if (percentualConcluido >= 0 && percentualConcluido <= 100) {
            this.percentualConcluido = percentualConcluido;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%d%%)", titulo, percentualConcluido);
    }
}