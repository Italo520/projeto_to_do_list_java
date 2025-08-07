package br.com.todolist.ui.telaPrincipal; // Ou um pacote de sua preferência

import br.com.todolist.models.Subtarefa;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * Esta classe é responsável por "desenhar" cada item da JList de subtarefas.
 * Ela usa um JCheckBox para exibir o título e o status (marcado/desmarcado)
 * da subtarefa.
 */
public class SubtarefaCellRenderer extends JPanel implements ListCellRenderer<Subtarefa> {

    private final JCheckBox checkBox;

    public SubtarefaCellRenderer() {
        setLayout(new BorderLayout());
        setOpaque(true);
        checkBox = new JCheckBox();
        // Garante que o fundo do checkbox seja transparente para herdar a cor do painel
        checkBox.setOpaque(true);
        add(checkBox, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Subtarefa> list,
                                                  Subtarefa subtarefa,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        // Configura o componente com os dados da subtarefa atual
        checkBox.setText(subtarefa.getTitulo());
        // A classe Subtarefa já tem um método 'isStatus'
        checkBox.setSelected(subtarefa.isStatus());

        // Define as cores de fundo e da fonte com base na seleção da lista
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            checkBox.setBackground(list.getSelectionBackground());
            checkBox.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            checkBox.setBackground(list.getBackground());
            checkBox.setForeground(list.getForeground());
        }

        return this;
    }
}