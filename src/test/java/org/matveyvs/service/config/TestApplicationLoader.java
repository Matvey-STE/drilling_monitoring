package org.matveyvs.service.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.annotation.Rollback;

@TestConfiguration
@Rollback(false)
public class TestApplicationLoader {
}
