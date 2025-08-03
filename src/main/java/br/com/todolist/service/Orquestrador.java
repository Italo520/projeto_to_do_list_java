// Em: src/main/java/br/com/todolist/service/Orquestrador.java
package br.com.todolist.service;

import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.models.Usuario;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;


public class Orquestrador {

    private final GerenteDeTarefas gerenteDeTarefas;
    private final GerenteDeEventos gerenteDeEventos;

   
    public Orquestrador(Usuario usuario) {
        GerenteDeDadosDoUsuario dadosDoUsuario = new GerenteDeDadosDoUsuario(usuario);
        this.gerenteDeTarefas = new GerenteDeTarefas(dadosDoUsuario);
        this.gerenteDeEventos = new GerenteDeEventos(dadosDoUsuario);
    }



    public void cadastrarTarefa(Tarefa tarefa) {
        this.gerenteDeTarefas.cadastrarTarefa(tarefa);
    }

    public List<Tarefa> listarTodasTarefas() {
        return this.gerenteDeTarefas.listarTodasTarefas();
    }

    public void excluirTarefa(Tarefa tarefa) {
        this.gerenteDeTarefas.excluirTarefa(tarefa);
    }
    //editar tarefa edita a tarefa da de editar tarefa
    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline, int novaPrioridade) {
        this.gerenteDeTarefas.editarTarefa(tarefaOriginal, novoTitulo, novaDescricao, novoDeadline, novaPrioridade);
    }
    //atualizar tarefa atualiza o objeto tarefa com suas subtarefas
    public void atualizarTarefa(Tarefa tarefa) {
        this.gerenteDeTarefas.atualizarTarefa(tarefa);
    }

    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return this.gerenteDeTarefas.listarTarefasPorDia(dia);
    }

    public List<Tarefa> listarTarefasCriticas() {
        return this.gerenteDeTarefas.listarTarefasCriticas();
    }
    
    public boolean cadastrarEvento(Evento evento) {
        return this.gerenteDeEventos.cadastrarEvento(evento);
    }


    public List<Evento> listarTodosEventos() {
        return this.gerenteDeEventos.listarTodosEventos();
    }

    public void excluirEvento(Evento evento) {
        this.gerenteDeEventos.excluirEvento(evento);
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        this.gerenteDeEventos.editarEvento(eventoOriginal, novoTitulo, novaDescricao, novoDeadline);
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return this.gerenteDeEventos.listarEventosPorDia(dia);
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return this.gerenteDeEventos.listarEventosPorMes(mes);
    }


}