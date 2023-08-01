package com.scm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.scm") 
public class SmarterContactManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmarterContactManagerApplication.class, args);
		
		System.out.println("Started......");
	}

	
}
