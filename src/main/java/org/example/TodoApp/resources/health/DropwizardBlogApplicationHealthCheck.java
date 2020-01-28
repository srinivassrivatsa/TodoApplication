package org.example.TodoApp.resources.health;
import com.codahale.metrics.health.HealthCheck;
import org.example.TodoApp.resources.service.TodosService;
public class DropwizardBlogApplicationHealthCheck extends HealthCheck {
    private static final String HEALTHY = "The Dropwizard blog Service is healthy for read and write";
    private static final String UNHEALTHY = "The Dropwizard blog Service is not healthy. ";
    private static final String MESSAGE_PLACEHOLDER = "{}";

    private final TodosService todosService;

    public DropwizardBlogApplicationHealthCheck(TodosService todosService) {
        this.todosService = todosService;
    }

    @Override
    public Result check() throws Exception {
        String mySqlHealthStatus = todosService.performHealthCheck();

        if (mySqlHealthStatus == null) {
            return Result.healthy(HEALTHY);
        } else {
            return Result.unhealthy(UNHEALTHY + MESSAGE_PLACEHOLDER, mySqlHealthStatus);
        }
    }
}
