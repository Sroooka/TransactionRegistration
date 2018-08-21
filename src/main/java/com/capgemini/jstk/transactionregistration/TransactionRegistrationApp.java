package com.capgemini.jstk.transactionregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionRegistrationApp 
{
	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "mysql");
		SpringApplication.run(TransactionRegistrationApp.class, args);
	}
}
