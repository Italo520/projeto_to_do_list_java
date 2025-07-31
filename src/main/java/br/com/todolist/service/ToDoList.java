package br.com.todolist.service;

import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.persistence.GerenciadorDePersistencia;

import java.util.List;


public class ToDoList {

    private ManipuladorDeTarefas manipuladorDeTarefas;
    private ManipuladorDeEventos manipuladorDeEventos;
    private GerenciadorDePersistencia persistencia; 

    public ToDoList() {
        this.manipuladorDeTarefas = new ManipuladorDeTarefas();
        this.manipuladorDeEventos = new ManipuladorDeEventos();
        // this.persistencia = new GerenciadorDePersistencia("dados.xml");
        // carregarDados(); // Carrega os dados do arquivo ao iniciar
    }

    // --- Métodos Delegados para Tarefas ---

    public void cadastrarTarefa(Tarefa tarefa) {
        manipuladorDeTarefas.cadastrarTarefa(tarefa);
        // salvarDados(); // Salva após cada alteração importante
    }

    public List<Tarefa> getTodasTarefas() {
        return manipuladorDeTarefas.listarTodasTarefas();
    }
    
    // ... adicione aqui todos os outros métodos de ManipuladorDeTarefas,
    // apenas repassando a chamada. Ex:
    // public List<Tarefa> getTarefasCriticas() {
    //     return manipuladorDeTarefas.listarTarefasCriticas();
    // }

    // --- Métodos Delegados para Eventos ---

    public boolean cadastrarEvento(Evento evento) {
        boolean sucesso = manipuladorDeEventos.cadastrarEvento(evento);
        if (sucesso) {
            salvarDados();
        }
        return sucesso;
    }

    public List<Evento> getTodosEventos() {
        return manipuladorDeEventos.listarTodosEventos();
    }
    
    

    // --- Métodos de Persistência ---

    public void salvarDados() {
        persistencia.salvar(getTodasTarefas(), getTodosEventos());
    }
    @SuppressWarnings("unchecked")
    public void carregarDados() {
        Object[] dados = persistencia.carregar();
        if (dados != null) {
            manipuladorDeTarefas.setTarefas((List<Tarefa>) dados[0]);
            manipuladorDeEventos.setEventos((List<Evento>) dados[1]);
        }
    }


    public void excluirTarefa(Tarefa tarefa) {
        manipuladorDeTarefas.excluirTarefa(tarefa);
        salvarDados(); 
    }

}