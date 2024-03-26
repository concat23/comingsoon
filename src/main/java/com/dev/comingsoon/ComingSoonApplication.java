package com.dev.comingsoon;

import com.dev.comingsoon.config.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RSAKeyRecord.class)
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ComingSoonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComingSoonApplication.class, args);
	}

}
