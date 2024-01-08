package com.example.cms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GmsApplication {

	private static Logger LOG = LoggerFactory
			.getLogger(GmsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GmsApplication.class, args);
		LOG.info("APPLICATION IS RUNNING");
	}
}
