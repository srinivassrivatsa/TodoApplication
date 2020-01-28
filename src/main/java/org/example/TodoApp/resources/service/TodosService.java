package org.example.TodoApp.resources.service;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

import org.example.TodoApp.resources.dao.TodosDao;
import org.example.TodoApp.resources.model.Todo;

public abstract class TodosService {
    private static final String TODO_NOT_FOUND = "Todo id %s not found.";
    private static final String DATABASE_REACH_ERROR =
            "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
    private static final String DATABASE_CONNECTION_ERROR =
            "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
    private static final String DATABASE_UNEXPECTED_ERROR =
            "Unexpected error occurred while attempting to reach the database. Details: ";
    private static final String SUCCESS = "Success...";
    private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting todo.";

    @CreateSqlObject
    abstract TodosDao todosDao();

    public List<Todo> getTodos() {
        return todosDao().getTodos();
    }

    public Todo getTodo(int id) {
        Todo todo = todosDao().getTodo(id);
        if (Objects.isNull(todo)) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return todo;
    }

    public Todo createTodo(Todo todo) {
        todosDao().createTodo(todo);
        return todosDao().getTodo(todosDao().lastInsertId());
    }

    public Todo editTodo(Todo todo) {
        if (Objects.isNull(todosDao().getTodo(todo.getId()))) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        todosDao().editTodo(todo);
        return todosDao().getTodo(todo.getId());
    }

    public String deleteTodo(final int id) {
        int result = todosDao().deleteTodo(id);
        switch (result) {
            case 1:
                return SUCCESS;
            case 0:
                throw new WebApplicationException(Status.NOT_FOUND);
            default:
                throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
    }

    public String performHealthCheck() {
        try {
            todosDao().getTodos();
        } catch (UnableToObtainConnectionException ex) {
            return checkUnableToObtainConnectionException(ex);
        } catch (UnableToExecuteStatementException ex) {
            return checkUnableToExecuteStatementException(ex);
        } catch (Exception ex) {
            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
        }
        return null;
    }

    private String checkUnableToObtainConnectionException(UnableToObtainConnectionException ex) {
        if (ex.getCause() instanceof java.sql.SQLNonTransientConnectionException) {
            return DATABASE_REACH_ERROR + ex.getCause().getLocalizedMessage();
        } else if (ex.getCause() instanceof java.sql.SQLException) {
            return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
        } else {
            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
        }
    }

    private String checkUnableToExecuteStatementException(UnableToExecuteStatementException ex) {
        if (ex.getCause() instanceof java.sql.SQLSyntaxErrorException) {
            return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
        } else {
            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
        }
    }
}