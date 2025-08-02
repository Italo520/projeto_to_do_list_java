package br.com.todolist.service;

import br.com.todolist.models.Evento;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class GerenteDeEventos {

    private final GerenteDeDadosDoUsuario gerenciadorDeDados;

    public GerenteDeEventos(GerenteDeDadosDoUsuario gerenciadorDeDados) {
        this.gerenciadorDeDados = gerenciadorDeDados;
    }

    public boolean cadastrarEvento(Evento novoEvento) {
        boolean dataDisponivel = gerenciadorDeDados.getEventos().stream()
                .noneMatch(evento -> evento.getDeadline().isEqual(novoEvento.getDeadline()));

        if (dataDisponivel) {
            gerenciadorDeDados.getEventos().add(novoEvento);
            gerenciadorDeDados.salvarDados();
            return true;
        }

        System.out.println("Erro: Já existe um evento cadastrado para esta data.");
        return false;
    }

    public List<Evento> listarTodosEventos() {
        return gerenciadorDeDados.getEventos();
    }

    public void excluirEvento(Evento evento) {
        gerenciadorDeDados.getEventos().remove(evento);
        gerenciadorDeDados.salvarDados();
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        eventoOriginal.setTitulo(novoTitulo);
        eventoOriginal.setDescricao(novaDescricao);
        eventoOriginal.setDeadLine(novoDeadline);
        gerenciadorDeDados.salvarDados();
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return gerenciadorDeDados.getEventos().stream()
                .filter(evento -> evento.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return gerenciadorDeDados.getEventos().stream()
                .filter(evento -> YearMonth.from(evento.getDeadline()).equals(mes))
                .collect(Collectors.toList());
    }
}