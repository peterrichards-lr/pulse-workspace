package com.liferay.sales.engineering.pulse;

import com.liferay.client.extension.util.spring.boot.ClientExtensionUtilSpringBootComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import static org.springframework.boot.SpringApplication.run;

@Import(ClientExtensionUtilSpringBootComponentScan.class)
@SpringBootApplication
public class PulseApplication {

    public static void main(String[] args) {
        run(PulseApplication.class, args);
    }

}