package com.example.rest_security_api.integration.annotation;


import com.example.rest_security_api.container.initialization.Localstack;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(initializers = Localstack.Initializer.class)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
public @interface IT {
}
