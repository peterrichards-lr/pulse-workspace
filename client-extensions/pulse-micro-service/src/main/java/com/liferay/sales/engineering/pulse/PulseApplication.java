package com.liferay.sales.engineering.pulse;

import com.liferay.client.extension.util.spring.boot.ClientExtensionUtilSpringBootComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import java.util.concurrent.Executor;

import static org.springframework.boot.SpringApplication.run;

@Import(ClientExtensionUtilSpringBootComponentScan.class)
@SpringBootApplication
@EnableWebFluxSecurity
@EnableAsync
public class PulseApplication {
    public static void main(String[] args) {
        run(PulseApplication.class, args);
    }

    @Bean(name = "asyncTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("PulseAsyncThread-");
        executor.initialize();
        return executor;
    }
}