package br.com.thaynara;

import javax.swing.*;
import java.awt.*;

public class TaskRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Task task) {
            label.setText(task.toString());
            label.setForeground(task.isDone() ? Color.GRAY : Color.BLACK);
        }

        return label;
    }
}
