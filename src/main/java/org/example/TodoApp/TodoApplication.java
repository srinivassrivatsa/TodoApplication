package org.example.TodoApp;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Environment;

import javax.sql.DataSource;


import org.example.TodoApp.resources.auth.DropwizardBlogAuthenticator;
import org.example.TodoApp.resources.auth.DropwizardBlogAuthorizer;
import org.example.TodoApp.resources.auth.User;
import org.example.TodoApp.resources.config.TodoAppConfiguration;
import org.example.TodoApp.resources.health.DropwizardBlogApplicationHealthCheck;
import org.example.TodoApp.resources.TodosResource;
import org.example.TodoApp.resources.service.TodosService;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
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
        // Datasource configuration
        final DataSource dataSource =
                configuration.getDataSourceFactory().build(environment.metrics(), SQL);
        DBI dbi = new DBI(dataSource);

        // Register Health Check
        DropwizardBlogApplicationHealthCheck healthCheck =
                new DropwizardBlogApplicationHealthCheck(dbi.onDemand(TodosService.class));
        environment.healthChecks().register(DROPWIZARD_BLOG_SERVICE, healthCheck);

        // Register resources
        environment.jersey().register(new TodosResource(dbi.onDemand(TodosService.class)));
    }
}
