package org.matveyvs;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationLoader {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationLoader.class, args);
    }

    @PostConstruct
    public void run() {
    }

}
