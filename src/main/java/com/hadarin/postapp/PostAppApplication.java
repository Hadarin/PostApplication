package com.hadarin.postapp;

import com.hadarin.postapp.configuration.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class PostAppApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(PostAppApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(PostAppApplication.class, args);
	}

}
