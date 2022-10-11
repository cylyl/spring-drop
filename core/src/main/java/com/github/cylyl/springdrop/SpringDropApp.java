package com.github.cylyl.springdrop;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class SpringDropApp extends Application<Configuration> implements CommandLineRunner {

	private Configuration configuration;
	private Environment environment;

	public Configuration getConfiguration() {
		return configuration;
	}

	public Environment getEnvironment() {
		return environment;
	}

	private static String readBanner(){
		try {
			String banner = new String(Files.readAllBytes(Paths.get("banner.txt")),
					StandardCharsets.UTF_8);
			return banner;
		}catch(IOException ex){
			return "Banner not found";
		}
	}

	@Override
	public void initialize(Bootstrap<Configuration> bootstrap) {
		super.initialize(bootstrap);
		bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
	}

	@Override
	public void run(Configuration c, Environment e) throws Exception {
		this.configuration = c;
		this.environment = e;

		e.jersey().register(new HelloRESTController(e.getValidator()));
	}

	public void start(String[] args) throws Exception {

		String[] newArgs = new String[args.length + 2];
		newArgs[0] = "server";
		newArgs[1] = "/dw.yml";
		for (int i = 0; i < args.length; i++) {
			newArgs[i+2] = args[i];
		}
		SpringApplication application = new SpringApplication(SpringDropApp.class);
		application.setBanner(new Banner(){
			@Override
			public void printBanner(org.springframework.core.env.Environment environment, Class<?> sourceClass, PrintStream out) {
				out.print(readBanner());
			}
		});
		application.run(newArgs);
	}
}
