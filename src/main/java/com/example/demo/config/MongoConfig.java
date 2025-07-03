package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.IOException;

@Configuration
@Profile("prod")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${MONGO_ENDPOINT}")
    private String mongoEndpoint;

    @Value("${MONGO_WRITE_CREDS:/mnt/mongo/write-creds.json}")
    private String credentialsFilePath;

    @Override
    protected String getDatabaseName() {
        // The database name will be taken from the connection string
        return "admin";
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        try {
            // Load credentials from JSON file
            MongoCredentials credentials = loadMongoCredentials();
            String username = credentials.getUsername();
            String password = credentials.getPassword();

            // Build connection string with authentication
            String connectionString = "mongodb+srv://" + username + ":" + password + "@" + mongoEndpoint;

            // Create MongoDB client with connection string
            return MongoClients.create(connectionString);
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure MongoDB client", e);
        }
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    private MongoCredentials loadMongoCredentials() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(credentialsFilePath), MongoCredentials.class);
    }
}
