package com.example.demo.config;

import lombok.Data;

@Data
public class MongoCredentials {
    private String username;
    private String password;
    private String someOtherProperty;
}
