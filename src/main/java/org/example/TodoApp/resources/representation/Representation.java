package org.example.TodoApp.resources.representation;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Representation<T> {
    private long code;

    @Length(max = 3)
    private T todo;

    public Representation() {
    }

    public Representation(long code, T todo) {
        this.code = code;
        this.todo = todo;
    }

    @JsonProperty
    public long getCode() {
        return code;
    }

    @JsonProperty
    public T getTodo() {
        return todo;
    }
}