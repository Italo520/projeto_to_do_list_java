package br.com.poo.todolist.service;

import br.com.poo.todolist.models.Evento;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ManipuladorDeEventos {

    private List<Evento> eventos;

    public ManipuladorDeEventos() {
        this.eventos = new ArrayList<>();
    }

    public boolean cadastrarEvento(Evento novoEvento) {
        boolean dataDisponivel = eventos.stream()
                .noneMatch(evento -> evento.getDataEvento().isEqual(novoEvento.getDataEvento()));

        if (dataDisponivel) {
            this.eventos.add(novoEvento);
            return true;
        }
        
        System.out.println("Erro: Já existe um evento cadastrado para esta data.");
        return false;
    }

    public List<Evento> listarTodosEventos() {
        return this.eventos;
    }

    public void excluirEvento(Evento evento) {
        this.eventos.remove(evento);
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novaData) {
        eventoOriginal.setTitulo(novoTitulo);
        eventoOriginal.setDescricao(novaDescricao);
        eventoOriginal.setDataEvento(novaData);
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return eventos.stream()
                .filter(evento -> evento.getDataEvento().isEqual(dia))
                .collect(Collectors.toList());
    }
    
    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return eventos.stream()
                .filter(evento -> YearMonth.from(evento.getDataEvento()).equals(mes))
                .collect(Collectors.toList());
    }
    
    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }
}