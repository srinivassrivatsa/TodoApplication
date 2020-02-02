package org.example.TodoApp.resources.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import org.example.TodoApp.resources.mapper.TodosMapper;
import org.example.TodoApp.resources.model.Todo;

@RegisterMapper(TodosMapper.class)
public interface TodosDao {

    @SqlQuery("select * from todos;")
    public List<Todo> getTodos();

    @SqlQuery("select * from todos where id = :id")
    public Todo getTodo(@Bind("id") final int id);

    @SqlUpdate("insert into todos(taskname, description) values(:taskname, :description)")
    void createTodo(@BindBean final Todo todo);

    @SqlUpdate("update todos set taskname = coalesce(:taskname, taskname), description = coalesce(:description, description) where id = :id")
    void editTodo(@BindBean final Todo todo);

    @SqlUpdate("delete from todos where id = :id")
    int deleteTodo(@Bind("id") final int id);

    @SqlQuery("select last_insert_id();")
    public int lastInsertId();
}