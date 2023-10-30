package org.matveyvs.config;


import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@ComponentScan("org.matveyvs")
@EnableTransactionManagement
public class ApplicationConfiguration {

}
