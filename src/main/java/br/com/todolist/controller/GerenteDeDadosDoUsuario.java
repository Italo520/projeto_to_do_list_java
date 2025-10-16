package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.repository.Repositorio;
import java.util.ArrayList;
import java.util.List;

public class GerenteDeDadosDoUsuario {

    private final Repositorio repositorio;
    private List<Tarefa> tarefas;
    private List<Evento> eventos;

    public GerenteDeDadosDoUsuario(Repositorio repositorio) {
        this.repositorio = repositorio;
        carregarDados();
    }

    private void carregarDados() {
        DadosUsuario dados = (DadosUsuario) repositorio.carregar(DadosUsuario.class);
        if (dados != null) {
            this.tarefas = dados.getTarefas() != null ? dados.getTarefas() : new ArrayList<>();
            this.eventos = dados.getEventos() != null ? dados.getEventos() : new ArrayList<>();
        } else {
            this.tarefas = new ArrayList<>();
            this.eventos = new ArrayList<>();
        }
    }

    public void salvarDados() {
        DadosUsuario dadosParaSalvar = new DadosUsuario(tarefas, eventos);
        repositorio.salvar(dadosParaSalvar);
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public List<Evento> getEventos() {
        return eventos;
    }
}