package com.example.rest_security_api.container.initialization;


import lombok.experimental.UtilityClass;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@UtilityClass
public class Minio {

    private static final int PORT = 9000;
    private static final int CONSOLE_PORT = 9001;

    public static final GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("quay.io/minio/minio"))
            .withExposedPorts(PORT, CONSOLE_PORT)
            .withEnv("MINIO_ACCESS_KEY", "accesskey")
            .withEnv("MINIO_SECRET_KEY", "secretkey")
            .withCommand("server /data --console-address :" + CONSOLE_PORT);

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
