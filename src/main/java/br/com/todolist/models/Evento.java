package br.com.todolist.models;
import java.time.LocalDate;

public class Evento extends Itens {

    public Evento(String titulo, String descricao, LocalDate deadline) {
        super(titulo, descricao, deadline);
    }

    public String toString() {
        return "Evento [titulo=" + getTitulo() + ", deadline=" + getDeadline() + "]";
    }
    
}

