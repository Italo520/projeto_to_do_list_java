// Copie e cole este código inteiro no seu arquivo Subtarefa.java

package br.com.todolist.models;

public class Subtarefa {

    private String titulo;

    public Subtarefa(String titulo) {
        this.titulo = titulo;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String toString() {
        return titulo;
    }
 
}