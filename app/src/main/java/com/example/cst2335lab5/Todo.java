package com.example.cst2335lab5;

public class Todo {
    private final long id;
    private final String text;
    private final boolean urgent;

    public Todo(long id, String text, boolean urgent) {
        this.id = id;
        this.text = text;
        this.urgent = urgent;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isUrgent() {
        return urgent;
    }

    @Override
    public String toString() {
        return urgent ? "⚠ " + text : text;
    }
}
