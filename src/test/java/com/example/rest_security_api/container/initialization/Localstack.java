package com.example.rest_security_api.container.initialization;


import lombok.experimental.UtilityClass;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@UtilityClass
public class Localstack {

    public static final LocalStackContainer container = new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
            .withServices(LocalStackContainer.Service.S3)
            .withReuse(true);


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
