package br.com.todolist.service;

import br.com.todolist.models.Tarefa;
import br.com.todolist.models.Evento;
import java.util.List;

public class DadosUsuario {
    private List<Tarefa> tarefas;
    private List<Evento> eventos;

    public DadosUsuario() {
    }

    public DadosUsuario(List<Tarefa> tarefas, List<Evento> eventos) {
        this.tarefas = tarefas;
        this.eventos = eventos;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }
}