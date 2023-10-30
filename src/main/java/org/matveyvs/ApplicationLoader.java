package org.matveyvs;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.matveyvs.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
@AllArgsConstructor
public class ApplicationLoader {
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationLoader.class, args);
    }

    @PostConstruct
    public void run() {

        log.info("Start application");
        userService.findAll();

    }

}
