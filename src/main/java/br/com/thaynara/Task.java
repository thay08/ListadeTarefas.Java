package br.com.thaynara;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String description;
    private LocalDateTime dateTime;
    private boolean done;

    public Task(String description, LocalDateTime dateTime) {
        this.description = description;
        this.dateTime = dateTime;
        this.done = false;
    }

    public void toggleDone() {
        this.done = !this.done;
    }

    public boolean isDone() {
        return done;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return description + " (" +
                dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                ") - " + (done ? "✓ Concluído" : "⏳ Pendente");
    }
}