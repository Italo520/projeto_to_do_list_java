package br.com.todolist.ui.telaPrincipal;

import br.com.todolist.models.Subtarefa;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;


public class SubtarefaCellRenderer extends JPanel implements ListCellRenderer<Subtarefa> {

    private final JCheckBox checkBox;

    public SubtarefaCellRenderer() {
        setLayout(new BorderLayout());
        setOpaque(true);
        checkBox = new JCheckBox();
        checkBox.setOpaque(true);
        add(checkBox, BorderLayout.CENTER);
    }

    public Component getListCellRendererComponent(JList<? extends Subtarefa> list,
                                                  Subtarefa subtarefa,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        checkBox.setText(subtarefa.getTitulo());
        checkBox.setSelected(subtarefa.isStatus());

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