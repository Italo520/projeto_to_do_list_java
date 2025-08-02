package br.com.todolist.service;
import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.models.Usuario;
import br.com.todolist.persistence.GerenciadorDePersistenciaJson;
import java.util.ArrayList;
import java.util.List;

public class GerenteDeDadosDoUsuario {

    private final GerenciadorDePersistenciaJson persistencia;
    private List<Tarefa> tarefas;
    private List<Evento> eventos;

    public GerenteDeDadosDoUsuario(Usuario usuario) {
        String nomeArquivo = "arquivos/" + usuario.getEmail().replace("@", "_").replace(".", "_") + "_dados.json";
        this.persistencia = new GerenciadorDePersistenciaJson(nomeArquivo);
        carregarDados();
    }
    private void carregarDados() {
        Object[] dados = (Object[]) persistencia.carregar(Object[].class);
        if (dados != null && dados.length == 2) {
            this.tarefas = (List<Tarefa>) dados[0];
            this.eventos = (List<Evento>) dados[1];
        } else {
            this.tarefas = new ArrayList<>();
            this.eventos = new ArrayList<>();
        }
    }

    public void salvarDados() {
        Object[] dadosParaSalvar = {tarefas, eventos};
        persistencia.salvar(dadosParaSalvar);
    }
    
    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public List<Evento> getEventos() {
        return eventos;
    }
}