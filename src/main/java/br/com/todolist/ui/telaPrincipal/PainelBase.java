package br.com.todolist.ui.telaPrincipal;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public abstract class PainelBase extends JPanel {

    public PainelBase() {
    }


    protected final void inicializarLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel painelDeBotoes = criarPainelDeBotoes();
        add(painelDeBotoes, BorderLayout.NORTH);

        JPanel painelDeConteudo = criarPainelDeConteudo();
        add(painelDeConteudo, BorderLayout.CENTER);
    }


    protected abstract JPanel criarPainelDeBotoes();

    protected abstract JPanel criarPainelDeConteudo();
    
}