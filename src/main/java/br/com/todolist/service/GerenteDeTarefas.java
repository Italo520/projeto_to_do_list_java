package br.com.todolist.service;

import br.com.todolist.models.Tarefa;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class GerenteDeTarefas {

    private final GerenteDeDadosDoUsuario gerenciadorDeDados;
    private final String emailUsuario;

    public GerenteDeTarefas(GerenteDeDadosDoUsuario gerenciadorDeDados, String emailUsuario) {
        this.gerenciadorDeDados = gerenciadorDeDados;
        this.emailUsuario = emailUsuario;
    }

    public void cadastrarTarefa(Tarefa tarefa) {
        // Verifica se a tarefa a ser cadastrada pertence ao usuário correto
        if (tarefa.getCriado_por().equals(this.emailUsuario)) {
            gerenciadorDeDados.getTarefas().add(tarefa);
            gerenciadorDeDados.salvarDados();
        } else {
            System.err.println("Erro: Tentativa de cadastrar uma tarefa para outro usuário.");
        }
    }

    // Retorna apenas as tarefas criadas pelo usuário logado
    public List<Tarefa> listarTodasTarefas() {
        return gerenciadorDeDados.getTarefas().stream()
                .filter(tarefa -> this.emailUsuario.equals(tarefa.getCriado_por()))
                .collect(Collectors.toList());
    }

    public void excluirTarefa(Tarefa tarefa) {
        // A exclusão precisa ser feita apenas na lista do usuário logado
        if (this.emailUsuario.equals(tarefa.getCriado_por())) {
            gerenciadorDeDados.getTarefas().remove(tarefa);
            gerenciadorDeDados.salvarDados();
        } else {
            System.err.println("Aviso: Tentativa de excluir tarefa de outro usuário.");
        }
    }

    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) {
        if (this.emailUsuario.equals(tarefaOriginal.getCriado_por())) {
            tarefaOriginal.setTitulo(novoTitulo);
            tarefaOriginal.setDescricao(novaDescricao);
            tarefaOriginal.setDeadLine(novoDeadline);
            tarefaOriginal.setPrioridade(novaPrioridade);
            gerenciadorDeDados.salvarDados();
        } else {
            System.err.println("Aviso: Tentativa de editar tarefa de outro usuário.");
        }
    }

    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return gerenciadorDeDados.getTarefas().stream()
                .filter(tarefa -> this.emailUsuario.equals(tarefa.getCriado_por()))
                .filter(tarefa -> tarefa.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasCriticas() {
        LocalDate hoje = LocalDate.now();
        return gerenciadorDeDados.getTarefas().stream()
                .filter(tarefa -> this.emailUsuario.equals(tarefa.getCriado_por()))
                .filter(tarefa -> {
                    long diasRestantes = ChronoUnit.DAYS.between(hoje, tarefa.getDeadline());
                    return (diasRestantes - tarefa.getPrioridade()) < 0;
                })
                .collect(Collectors.toList());
    }

    public void atualizarTarefa(Tarefa tarefa) {
        if (gerenciadorDeDados.getTarefas().contains(tarefa)) {
            if (this.emailUsuario.equals(tarefa.getCriado_por())) {
                gerenciadorDeDados.salvarDados();
                System.out.println("Tarefa atualizada e dados salvos.");
            } else {
                System.err.println("Aviso: Tentativa de atualizar tarefa de outro usuário.");
            }
        } else {
            System.err.println("Aviso: Tentativa de atualizar uma tarefa não encontrada.");
        }
    }

}