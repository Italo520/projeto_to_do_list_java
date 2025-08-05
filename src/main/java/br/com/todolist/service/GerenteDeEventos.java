package br.com.todolist.service;

import br.com.todolist.models.Evento;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class GerenteDeEventos {

    private final GerenteDeDadosDoUsuario gerenciadorDeDados;
    private final String emailUsuario;

    public GerenteDeEventos(GerenteDeDadosDoUsuario gerenciadorDeDados, String emailUsuario) {
        this.gerenciadorDeDados = gerenciadorDeDados;
        this.emailUsuario = emailUsuario;
    }

    public boolean cadastrarEvento(Evento novoEvento) {
        // Verifica se o evento a ser cadastrado pertence ao usuário correto
        if (!novoEvento.getCriado_por().equals(this.emailUsuario)) {
            System.err.println("Erro: Tentativa de cadastrar um evento para outro usuário.");
            return false;
        }

        // A lógica de verificação de data disponível pode ser feita aqui,
        // garantindo que não haja dois eventos no mesmo dia para o mesmo usuário.
        boolean dataDisponivel = gerenciadorDeDados.getEventos().stream()
                .filter(evento -> this.emailUsuario.equals(evento.getCriado_por()))
                .noneMatch(evento -> evento.getDeadline().isEqual(novoEvento.getDeadline()));

        if (dataDisponivel) {
            gerenciadorDeDados.getEventos().add(novoEvento);
            gerenciadorDeDados.salvarDados();
            return true;
        }

        System.out.println("Erro: Já existe um evento cadastrado para esta data para o usuário.");
        return false;
    }

    // Retorna apenas os eventos criados pelo usuário logado
    public List<Evento> listarTodosEventos() {
        return gerenciadorDeDados.getEventos().stream()
                .filter(evento -> this.emailUsuario.equals(evento.getCriado_por()))
                .collect(Collectors.toList());
    }

    public void excluirEvento(Evento evento) {
        // A exclusão precisa ser feita apenas na lista do usuário logado
        if (this.emailUsuario.equals(evento.getCriado_por())) {
            gerenciadorDeDados.getEventos().remove(evento);
            gerenciadorDeDados.salvarDados();
        } else {
            System.err.println("Aviso: Tentativa de excluir evento de outro usuário.");
        }
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        if (this.emailUsuario.equals(eventoOriginal.getCriado_por())) {
            eventoOriginal.setTitulo(novoTitulo);
            eventoOriginal.setDescricao(novaDescricao);
            eventoOriginal.setDeadLine(novoDeadline);
            gerenciadorDeDados.salvarDados();
        } else {
            System.err.println("Aviso: Tentativa de editar evento de outro usuário.");
        }
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return gerenciadorDeDados.getEventos().stream()
                .filter(evento -> this.emailUsuario.equals(evento.getCriado_por()))
                .filter(evento -> evento.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return gerenciadorDeDados.getEventos().stream()
                .filter(evento -> this.emailUsuario.equals(evento.getCriado_por()))
                .filter(evento -> YearMonth.from(evento.getDeadline()).equals(mes))
                .collect(Collectors.toList());
    }
}