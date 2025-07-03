package com.example.config;

import lombok.Data;

@Data
public class MongoCredentials {
    private String username;
    private String password;
    private String someOtherProperty;
}
