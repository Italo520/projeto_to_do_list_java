package br.com.todolist.models;

public class Subtarefa {

    private String titulo;
    private boolean status;

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

    public void mudarStatus(){
        if (status == false){
            status = true;
        } else {
            status = false;
        }
    }

    public String toString() {
        return titulo;
    }
 
}