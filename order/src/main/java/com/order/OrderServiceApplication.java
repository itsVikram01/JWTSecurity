package com.order;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@SpringBootApplication
@EnableCaching
public class OrderServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceApplication.class);

	public static void main(String[] args) {
		logger.info("Application started");

		logger.debug("Debugging message");
		logger.warn("Warning message");

		try {
			SpringApplication.run(OrderServiceApplication.class, args);
		} catch (Exception e) {
			logger.error("An error occurred", e);
		}

		System.out.println("Started............!!!");
	}

}
