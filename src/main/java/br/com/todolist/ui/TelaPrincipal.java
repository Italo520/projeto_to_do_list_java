package br.com.poo.todolist.ui;

import br.com.poo.todolist.model.Evento;
import br.com.poo.todolist.model.Subtarefa;
import br.com.poo.todolist.model.Tarefa;
import br.com.poo.todolist.model.Usuario;
import br.com.poo.todolist.service.ToDoList;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    private ToDoList toDoList;
    private Usuario usuario;

    // Componentes de Tarefas
    private JList<Tarefa> listaTarefas;
    private DefaultListModel<Tarefa> modeloListaTarefas;
    private JList<Subtarefa> listaSubtarefas; 
    private DefaultListModel<Subtarefa> modeloListaSubtarefas;

    // Componentes de Eventos
    private JList<Evento> listaEventos;
    private DefaultListModel<Evento> modeloListaEventos;
    private JTextArea areaDetalhesEvento;

    public TelaPrincipal(Usuario usuario) {
        this.toDoList = new ToDoList();
        this.usuario = usuario;

        setTitle("Gerenciador de " + this.usuario.getNome());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        inicializarComponentes();
        adicionarListeners();
        atualizarListas();
    }

    private void inicializarComponentes() {
        JTabbedPane abas = new JTabbedPane();

        // --- PAINEL DE TAREFAS COM DETALHES ---
        modeloListaTarefas = new DefaultListModel<>();
        listaTarefas = new JList<>(modeloListaTarefas);

        modeloListaSubtarefas = new DefaultListModel<>();
        listaSubtarefas = new JList<>(modeloListaSubtarefas);
        // Painel de detalhes da tarefa
        JPanel painelDetalheTarefa = new JPanel(new BorderLayout());
        painelDetalheTarefa.setBorder(BorderFactory.createTitledBorder("Subtarefas da Tarefa Selecionada"));
        painelDetalheTarefa.add(new JScrollPane(listaSubtarefas), BorderLayout.CENTER);

        // Split Pane para dividir a lista principal e os detalhes
        JSplitPane splitPaneTarefas = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(listaTarefas), painelDetalheTarefa);
        splitPaneTarefas.setDividerLocation(400); // Posição inicial do divisor

        // Painel principal de tarefas que contém o split pane e os botões
        JPanel painelTarefas = new JPanel(new BorderLayout());
        painelTarefas.add(splitPaneTarefas, BorderLayout.CENTER);
        JPanel painelBotoesTarefas = new JPanel();
        painelBotoesTarefas.add(new JButton("Nova Tarefa")); // Estes botões podem ser melhorados depois
        painelBotoesTarefas.add(new JButton("Excluir Tarefa"));
        painelTarefas.add(painelBotoesTarefas, BorderLayout.SOUTH);

        // --- PAINEL DE EVENTOS COM DETALHES ---
        modeloListaEventos = new DefaultListModel<>();
        listaEventos = new JList<>(modeloListaEventos);
        
        areaDetalhesEvento = new JTextArea();
        areaDetalhesEvento.setEditable(false);
        areaDetalhesEvento.setLineWrap(true);
        areaDetalhesEvento.setWrapStyleWord(true);
        areaDetalhesEvento.setBorder(BorderFactory.createTitledBorder("Descrição do Evento Selecionado"));

        JSplitPane splitPaneEventos = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(listaEventos), new JScrollPane(areaDetalhesEvento));
        splitPaneEventos.setDividerLocation(400);

        JPanel painelEventos = new JPanel(new BorderLayout());
        painelEventos.add(splitPaneEventos, BorderLayout.CENTER);
        JPanel painelBotoesEventos = new JPanel();
        painelBotoesEventos.add(new JButton("Novo Evento"));
        painelEventos.add(painelBotoesEventos, BorderLayout.SOUTH);


        abas.addTab("Tarefas", painelTarefas);
        abas.addTab("Eventos", painelEventos);

        add(abas);
    }

    private void adicionarListeners() {
        // Listener para a lista de TAREFAS
        listaTarefas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Tarefa tarefaSelecionada = listaTarefas.getSelectedValue();
                modeloListaSubtarefas.clear();
                if (tarefaSelecionada != null) {
                    // Povoa a lista de subtarefas
                    tarefaSelecionada.getSubtarefas().forEach(modeloListaSubtarefas::addElement);
                }
            }
        });

        // Listener para a lista de EVENTOS
        listaEventos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Evento eventoSelecionado = listaEventos.getSelectedValue();
                if (eventoSelecionado != null) {
                    areaDetalhesEvento.setText(eventoSelecionado.getDescricao());
                } else {
                    areaDetalhesEvento.setText("");
                }
            }
        });
        
        // Você pode re-adicionar os listeners dos botões aqui
    }

    private void atualizarListas() {
        modeloListaTarefas.clear();
        toDoList.getTodasTarefas().forEach(modeloListaTarefas::addElement);

        modeloListaEventos.clear();
        toDoList.getTodosEventos().forEach(modeloListaEventos::addElement);
    }
}