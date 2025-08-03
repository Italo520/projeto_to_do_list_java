package br.com.todolist.ui.telaPrincipal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class BarraFerramentas {
    public static JMenuBar criarBarraFerramentas(JFrame frame) {
        return criarBarraDeMenus(frame);
    }

    /**
     * Cria e configura a barra de menus (JMenuBar) principal da aplicação.
     * @param frame A janela principal, usada para ações como "fechar".
     * @return um objeto JMenuBar pronto para ser adicionado à janela.
     */
    public static JMenuBar criarBarraDeMenus(JFrame frame) {
        // 1. Crie a barra de menus principal
        JMenuBar menuBar = new JMenuBar();

        // 2. Crie os menus que irão na barra (ex: Arquivo, Editar, Ajuda)
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuAjuda = new JMenu("Ajuda");
        
        // Adiciona um "atalho" para o menu Arquivo (Alt + A)
        menuArquivo.setMnemonic(KeyEvent.VK_A);


        // 3. Crie os itens de cada menu
        
        // --- Itens do Menu Arquivo ---
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener((ActionEvent e) -> {
            // Ação para fechar a aplicação
            frame.dispose();
            System.exit(0);
        });

        // --- Itens do Menu Ajuda ---
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener((ActionEvent e) -> {
            // Ação para mostrar uma janela de diálogo
            JOptionPane.showMessageDialog(frame, 
                "Aplicação de Lista de Tarefas\nVersão 1.0", 
                "Sobre", 
                JOptionPane.INFORMATION_MESSAGE);
        });


        // 4. Adicione os itens aos seus respectivos menus
        menuArquivo.add(itemSair);
        menuAjuda.add(itemSobre);


        // 5. Adicione os menus à barra de menus principal
        menuBar.add(menuArquivo);
        menuBar.add(menuAjuda);

        // 6. Retorne a barra de menus completa
        return menuBar;
 
    
}
}