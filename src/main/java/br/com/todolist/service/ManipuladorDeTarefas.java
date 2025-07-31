package br.com.poo.todolist.service;

import br.com.poo.todolist.models.Subtarefa;
import br.com.poo.todolist.models.Tarefa;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ManipuladorDeTarefas {

    private List<Tarefa> tarefas;

    public ManipuladorDeTarefas() {
        this.tarefas = new ArrayList<>();
    }

    public void cadastrarTarefa(Tarefa tarefa) {
        this.tarefas.add(tarefa);
    }

    public List<Tarefa> listarTodasTarefas() {
        return this.tarefas;
    }

    public void excluirTarefa(Tarefa tarefa) {
        this.tarefas.remove(tarefa);
    }

   
    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade) {
        tarefaOriginal.setTitulo(novoTitulo);
        tarefaOriginal.setDescricao(novaDescricao);
        tarefaOriginal.setDeadline(novoDeadline);
        tarefaOriginal.setPrioridade(novaPrioridade);
    }


    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return tarefas.stream()
                .filter(tarefa -> tarefa.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

 
    public List<Tarefa> listarTarefasCriticas() {
        LocalDate hoje = LocalDate.now();
        return tarefas.stream()
                .filter(tarefa -> {
                    long diasRestantes = ChronoUnit.DAYS.between(hoje, tarefa.getDeadline());
                    return (diasRestantes - tarefa.getPrioridade()) < 0;
                })
                .collect(Collectors.toList());
    }

   
    public void adicionarSubtarefa(Tarefa tarefaPrincipal, Subtarefa subtarefa) {
        tarefaPrincipal.getSubtarefas().add(subtarefa);
        recalcularPercentualTarefa(tarefaPrincipal);
    }



    public void recalcularPercentualTarefa(Tarefa tarefa) {
        if (tarefa.getSubtarefas().isEmpty()) {
            return;
        }
        double media = tarefa.getSubtarefas().stream()
                .mapToInt(Subtarefa::getPercentualConcluido)
                .average()
                .orElse(0.0);

        // O tipo do percentual em Tarefa agora é double, então o cast (int) não é mais necessário
        tarefa.setPercentual(Math.round(media)); 
        
        // Nomes dos métodos ATUALIZADOS para usar dataConclusao
        if (tarefa.getPercentual() == 100 && tarefa.getDataConclusao() == null) {
            tarefa.setDataConclusao(LocalDate.now());
        }
    }
   
    
    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

}