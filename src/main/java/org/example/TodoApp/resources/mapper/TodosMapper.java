package org.example.TodoApp.resources.mapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import org.example.TodoApp.resources.model.Todo;
public class TodosMapper implements ResultSetMapper<Todo> {
    private static final String ID = "id";
    private static final String NAME = "taskname";
    private static final String DESC = "description";

    public Todo map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Todo(resultSet.getInt(ID), resultSet.getString(NAME), resultSet.getString(DESC));
    }
}
