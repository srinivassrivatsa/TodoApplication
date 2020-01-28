package org.example.TodoApp.resources.model;
import org.hibernate.validator.constraints.NotEmpty;

public class Todo {
    private int id;
    @NotEmpty
    private String taskname;
    @NotEmpty
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Todo() {
        super();
    }

    public Todo(int id, String name, String desc) {
        super();
        this.id = id;
        this.taskname = name;
        this.description = desc;
    }
}