// Conteúdo Modificado para: src/main/java/br/com/todolist/ui/telaPrincipal/PainelBase.java
package br.com.todolist.ui.telaPrincipal;

import java.awt.Dimension; // Importado para o setPreferredSize se necessário
import javax.swing.JPanel;

public abstract class PainelBase extends JPanel {

    public PainelBase() {
        super();
        // 1. O layout do painel base agora é NULO.
        setLayout(null); 
    }

    @Override
    public Dimension getPreferredSize() {
        // É uma boa prática definir um tamanho preferencial para o painel base,
        // especialmente porque ele não tem mais um layout manager para calcular seu tamanho.
        // Assumindo que este painel preenche a área principal da janela de 1280x720.
        return new Dimension(1265, 630); 
    }

    protected final void inicializarLayout() {
        // O setLayout(null) foi movido para o construtor.

        JPanel painelDeBotoes = criarPainelDeBotoes();
        JPanel painelDeConteudo = criarPainelDeConteudo();

        // 2. Definimos a posição e o tamanho exatos de cada painel.
        // setBounds(x, y, largura, altura)
        
        // O painel de botões ficará no topo, com 50px de altura.
        painelDeBotoes.setBounds(0, 0, 1270, 50); 

        // O painel de conteúdo ficará logo abaixo, preenchendo o resto.
        painelDeConteudo.setBounds(0, 50, 1260, 560);

        // 3. Adicionamos os painéis sem as restrições do BorderLayout.
        add(painelDeBotoes);
        add(painelDeConteudo);
    }

    protected abstract JPanel criarPainelDeBotoes();

    protected abstract JPanel criarPainelDeConteudo();
}