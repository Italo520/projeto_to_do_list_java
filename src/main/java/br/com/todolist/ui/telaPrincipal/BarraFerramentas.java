package br.com.todolist.ui.telaPrincipal;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class BarraFerramentas {

    public static JMenuBar criarBarraFerramentas() {
        JMenuBar barraFerramentas = new JMenuBar();

        barraFerramentas.add(new JMenu("Arquivo"));
        barraFerramentas.add(new JMenu("Editar"));
        barraFerramentas.add(new JMenu("Visualizar"));
        barraFerramentas.add(new JMenu("Ferramentas"));
        barraFerramentas.add(new JMenu("Ajuda"));

        return barraFerramentas;
    }

}
