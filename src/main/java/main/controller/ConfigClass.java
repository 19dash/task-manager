package main.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.modelmapper.ModelMapper;

@Configuration
public class ConfigClass  {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}