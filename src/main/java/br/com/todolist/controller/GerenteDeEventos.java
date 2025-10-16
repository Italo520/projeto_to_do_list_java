package br.com.todolist.controller;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Usuario;
import br.com.todolist.repository.Repositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GerenteDeEventos {

    private final Repositorio repositorio;
    private final Usuario usuario;

    public GerenteDeEventos(Repositorio repositorio, Usuario usuario) {
        this.repositorio = repositorio;
        this.usuario = usuario;
    }

    public void cadastrarEvento(Evento evento) {
        evento.setUsuario(usuario);
        usuario.getEventos().add(evento);
        repositorio.salvar(usuario);
    }

    public List<Evento> listarTodosEventos() {
        return usuario.getEventos();
    }

    public void excluirEvento(Evento evento) {
        usuario.getEventos().remove(evento);
        repositorio.salvar(usuario);
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        eventoOriginal.setTitulo(novoTitulo);
        eventoOriginal.setDescricao(novaDescricao);
        eventoOriginal.setDeadLine(novoDeadline);
        repositorio.salvar(usuario);
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return usuario.getEventos().stream()
                .filter(evento -> evento.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    public List<Evento> listarEventosPorMes(java.time.YearMonth mes) {
        return usuario.getEventos().stream()
                .filter(evento -> java.time.YearMonth.from(evento.getDeadline()).equals(mes))
                .collect(Collectors.toList());
    }
}