package br.com.poo.todolist.ui.frames;

import javax.swing.*;
import java.awt.BorderLayout;

public class TelaPrincipal {
    // Variáveis do designer
    private JTabbedPane painelComAbas;
    private JLabel labelBarraStatus;
    private JPanel painelStatus;
    private JPanel telaPrincipalPanel;

    // Construtor
    public TelaPrincipal() {
        // --- INÍCIO DA CORREÇÃO ---

        // 1. Inicializa o painel principal. Este é o passo mais importante!
        telaPrincipalPanel = new JPanel();
        // Define um layout para organizar os outros painéis dentro dele
        telaPrincipalPanel.setLayout(new BorderLayout());

        // 2. Inicializa os outros componentes que você desenhou
        painelComAbas = new JTabbedPane();
        painelStatus = new JPanel();
        labelBarraStatus = new JLabel("Pronto");

        // 3. Adiciona os componentes uns aos outros
        painelStatus.add(labelBarraStatus); // Adiciona o label ao painel de status

        // 4. Adiciona os painéis secundários ao painel principal
        telaPrincipalPanel.add(painelComAbas, BorderLayout.CENTER); // Painel com abas fica no centro
        telaPrincipalPanel.add(painelStatus, BorderLayout.SOUTH); // Painel de status fica na parte de baixo

        // --- FIM DA CORREÇÃO ---
    }

    // =======================================================
    // >> GARANTA QUE ESTE MÉTODO ESTÁ AQUI <<
    public JPanel getTelaPrincipalPanel() {
        return telaPrincipalPanel;
    }
    // =======================================================

    // Método para criar o menu
    public JMenuBar criarBarraDeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        menuArquivo.add(itemSair);
        JMenu menuConfiguracoes = new JMenu("Configurações");
        JMenu menuAjuda = new JMenu("Ajuda");
        menuBar.add(menuArquivo);
        menuBar.add(menuConfiguracoes);
        menuBar.add(menuAjuda);
        return menuBar;
    }
}