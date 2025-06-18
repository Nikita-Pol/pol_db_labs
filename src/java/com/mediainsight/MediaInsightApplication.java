package com.mediainsight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaInsightApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaInsightApplication.class, args);
        System.out.println("=== Media Insight API Started ===");
        System.out.println("Users доступнi за адресою: http://localhost:3030/api/users");
        System.out.println("Projects доступнi за адресою: http://localhost:3030/api/projects");
        System.out.println("Roles доступнi за адресою: http://localhost:3030/api/roles");
        System.out.println("MediaContent доступний за адресою: http://localhost:3030/api/media-content");
    }
}