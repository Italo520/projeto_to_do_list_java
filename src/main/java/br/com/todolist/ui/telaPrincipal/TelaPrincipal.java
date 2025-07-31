package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;


public class TelaPrincipal extends JFrame {

	public TelaPrincipal(){
		super("ToDoLIst");

		configurarJanela();
		montarLayout();
	}


	private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }



	private void montarLayout() {

        setJMenuBar(BarraFerramentas.criarBarraFerramentas());
        JTabbedPane painelComAbas = criarPaineis();

        setLayout(new BorderLayout());
        add(painelComAbas, BorderLayout.CENTER);
    }


	private JTabbedPane criarPaineis() {
        JTabbedPane aba = new JTabbedPane();

        aba.addTab("Tarefas", null, new PainelTarefas(), "Gerenciador de Tarefas");
        aba.addTab("Eventos", null, new PainelEventos(), "Gerenciador de Eventos");

        return aba;
    }

}