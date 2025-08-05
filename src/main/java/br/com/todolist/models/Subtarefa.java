package br.com.todolist.models;

public class Subtarefa {

    private String titulo;
    private boolean status;

    // Construtor padrão necessário para o Jackson
    public Subtarefa() {
    }

    public Subtarefa(String titulo) {
        this.titulo = titulo;
        this.status = false;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void mudarStatus() {
        if (status == false) {
            status = true;
        } else {
            status = false;
        }
    }

    // Getter para o status, necessário para a serialização do Jackson
    public boolean isStatus() {
        return status;
    }

    // Setter para o status, necessário para a desserialização do Jackson
    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toString() {
        return titulo;
    }

}