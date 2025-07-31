package br.com.todolist.ui.telaPrincipal;

import javax.swing.JPanel;

public abstract class PainelBase extends JPanel {
    public PainelBase(){
        inicializarPaineis();
    }

    protected abstract void inicializarPaineis();
}
