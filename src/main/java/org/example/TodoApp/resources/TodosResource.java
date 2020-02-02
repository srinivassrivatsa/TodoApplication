package org.example.TodoApp.resources;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.http.HttpStatus;

import com.codahale.metrics.annotation.Timed;
import org.example.TodoApp.resources.representation.Representation;
import org.example.TodoApp.resources.model.Todo;
import org.example.TodoApp.resources.service.TodosService;
@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class TodosResource {
    private final TodosService todosService;;

    public TodosResource(TodosService todosService) {
        this.todosService = todosService;
    }

    @GET
    @Timed
    public Representation<List<Todo>> getTodos() {
        try {
            return new Representation<List<Todo>>(HttpStatus.OK_200, todosService.getTodos());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Representation<List<Todo>>(HttpStatus.OK_200, todosService.getTodos());
    }

    @GET
    @Timed
    @Path("{id}")
    public Representation<Todo> getTodo(@PathParam("id") final int id) {
        return new Representation<Todo>(HttpStatus.OK_200, todosService.getTodo(id));
    }

    @POST
    @Timed
    public Representation<Todo> createTodo(@NotNull @Valid final Todo todo) {
        return new Representation<Todo>(HttpStatus.OK_200, todosService.createTodo(todo));
    }

    @PUT
    @Timed
    @Path("{id}")
    public Representation<Todo> editPart(@NotNull @Valid final Todo todo,
                                         @PathParam("id") final int id) {
        todo.setId(id);
        return new Representation<Todo>(HttpStatus.OK_200, todosService.editTodo(todo));
    }

    @DELETE
    @Timed
    @Path("{id}")
    public Representation<String> deleteTodo(@PathParam("id") final int id) {
        return new Representation<String>(HttpStatus.OK_200, todosService.deleteTodo(id));
    }
}
