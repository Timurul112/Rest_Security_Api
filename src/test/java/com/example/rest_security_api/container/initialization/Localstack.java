package com.example.rest_security_api.container.initialization;


import lombok.experimental.UtilityClass;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;

@UtilityClass
public class Localstack {
    public static final GenericContainer<?> container = new GenericContainer<>("minio/minio:latest")
            .withExposedPorts(9000)
            .withEnv("MINIO_ACCESS_KEY", "accesskey")
            .withEnv("MINIO_SECRET_KEY", "secretkey")
            .withCommand("server /data");


    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Integer originalPort = container.getExposedPorts().get(0);
            TestPropertyValues.of(
                            "localstack.port=" + container.getMappedPort(originalPort))
                    .applyTo(applicationContext);
        }
    }
}
