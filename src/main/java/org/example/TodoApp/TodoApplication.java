package org.example.TodoApp;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import javax.sql.DataSource;

import org.example.TodoApp.resources.config.TodoAppConfiguration;
import org.example.TodoApp.resources.health.DropwizardTodoApplicationHealthCheck;
import org.example.TodoApp.resources.TodosResource;
import org.example.TodoApp.resources.service.TodosService;
import org.skife.jdbi.v2.DBI;

public class TodoApplication extends Application<TodoAppConfiguration> {
    private static final String SQL = "sql";
    private static final String DROPWIZARD_BLOG_SERVICE = "Dropwizard blog service";
    private static final String BEARER = "Bearer";

    public static void main(String[] args) throws Exception {
        new TodoApplication().run(args);
    }

    @Override
    public void run(TodoAppConfiguration configuration, Environment environment) {
        final DataSource dataSource =
                configuration.getDataSourceFactory().build(environment.metrics(), SQL);
        DBI dbi = new DBI(dataSource);

        DropwizardTodoApplicationHealthCheck healthCheck =
                new DropwizardTodoApplicationHealthCheck(dbi.onDemand(TodosService.class));
        environment.healthChecks().register(DROPWIZARD_BLOG_SERVICE, healthCheck);

        environment.jersey().register(new TodosResource(dbi.onDemand(TodosService.class)));
    }
}
