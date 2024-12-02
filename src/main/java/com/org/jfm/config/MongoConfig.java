package com.org.jfm.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.quarkus.arc.DefaultBean;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class MongoConfig {

    @ConfigProperty(name = "quarkus.mongodb.connection-string")
    String connectionString;

    @Produces
    @DefaultBean
    public MongoClient mongoClient() {
        return MongoClients.create(connectionString);
    }
}