package org.example.bibliotecaDMR;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;


@SpringBootApplication
@RestController
@Configuration
@ComponentScan(basePackages = {"org.example", "org.example.LectorController"})
@ComponentScan(basePackages = {"org.example", "org.example.LibroController"})
@ComponentScan(basePackages = {"org.example", "org.example.PrestamoController"})
public class BibliotecaDmrApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaDmrApplication.class, args);
	}


	@Bean
	public Lector.LectorBuilder lectorBuilder() {
		return new Lector.LectorBuilder();
	}

	@Bean
	public Libro.LibroBuilder libroBuilder() {
		return new Libro.LibroBuilder();
	}

	@Bean
	public Prestamo.PrestamoBuilder prestamoBuilder() {
		return new Prestamo.PrestamoBuilder();
	}


	@Bean
	public ViewResolver viewResolver() {
		final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setCharacterEncoding("UTF-8");
		resolver.setTemplateEngine(templateEngine());
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver());
		return engine;
	}

	private ITemplateResolver templateResolver() {
		final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}
}
