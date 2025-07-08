package br.com.thaynara;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ToDoapp extends JFrame {
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskinput;
    private JSpinner dateTimeSpinner;

    private static final String SAVE_FILE = "tarefas.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm");


    public ToDoapp() {
        setTitle("Lista Tarefas - Thaynara");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setCellRenderer(new TaskRenderer());
        JScrollPane scrollPane = new JScrollPane(taskList);

        JPanel inputPanel = new JPanel(new FlowLayout());

        taskinput = new JTextField(16);
        dateTimeSpinner = new JSpinner(new SpinnerDateModel());
        dateTimeSpinner.setEditor(new JSpinner.DateEditor(dateTimeSpinner, "dd/MM/yyyy HH:mm"));

        JButton addButton = new JButton("Adicionar");
        JButton checkAllButton = new JButton("Verificar Conclusão");

        inputPanel.add(new JLabel("Tarefa:"));
        inputPanel.add(taskinput);
        inputPanel.add(new JLabel("Data e Hora:"));
        inputPanel.add(dateTimeSpinner);
        inputPanel.add(addButton);
        inputPanel.add(checkAllButton);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addTask());
        checkAllButton.addActionListener(e -> checkAllCompleted());

        taskList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = taskList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        Task task = taskListModel.get(index);
                        task.toggleDone();
                        taskList.repaint();
                    }
                }
            }
        });

        loadTasks();
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveTasks));
    }

    private void addTask() {
        String desc = taskinput.getText().trim();
        if (!desc.isEmpty()) {
            Date date = (Date) dateTimeSpinner.getValue();
            LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            Task task = new Task(desc, dateTime);
            taskListModel.addElement(task);
            taskinput.setText("");
        }
    }

    private void checkAllCompleted() {
        if (taskListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos uma tarefa 😊");
            return;
        }

        boolean allDone = true;
        for (int i = 0; i < taskListModel.size(); i++) {
            if (!taskListModel.get(i).isDone()) {
                allDone = false;
                break;
            }
        }

        if (allDone) {
            JOptionPane.showMessageDialog(this, "🎉 Parabéns! Você concluiu todas as tarefas!");
        } else {
            JOptionPane.showMessageDialog(this, "Ainda há tarefas pendentes.");
        }
    }

    private void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            for (int i = 0; i < taskListModel.size(); i++) {
                Task t = taskListModel.get(i);
                writer.println(t.getDescription() + ";" + t.getDateTime().format(FORMATTER) + ";" + t.isDone());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try {
            Files.lines(Paths.get(SAVE_FILE)).forEach(line -> {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String desc = parts[0];
                    LocalDateTime dateTime = LocalDateTime.parse(parts[1], FORMATTER);
                    boolean done = Boolean.parseBoolean(parts[2]);

                    Task task = new Task(desc, dateTime);
                    if (done) task.toggleDone();

                    taskListModel.addElement(task);
                }
            });
        } catch (IOException e) {
            System.out.println("Nenhum arquivo encontrado. Um novo será criado.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoapp().setVisible(true));
    }
}
