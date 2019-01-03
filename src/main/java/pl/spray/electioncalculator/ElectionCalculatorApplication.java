package pl.spray.electioncalculator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import lombok.extern.slf4j.Slf4j;
import pl.spray.electioncalculator.components.CandidatesComponent;
import pl.spray.electioncalculator.dao.AuthoritiesDAO;
import pl.spray.electioncalculator.dao.CandidatesDAO;
import pl.spray.electioncalculator.dao.ResultsDAO;
import pl.spray.electioncalculator.dao.UsersDAO;
import pl.spray.electioncalculator.dao.VoteCardDAO;
import pl.spray.electioncalculator.model.Authorities;
import pl.spray.electioncalculator.model.Results;
import pl.spray.electioncalculator.model.Users;
import pl.spray.electioncalculator.model.VoteCard;

@Slf4j
@PropertySource("classpath:additional.properties")
@SpringBootApplication
public class ElectionCalculatorApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(ElectionCalculatorApplication.class, args);
	}

	@Bean
	public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		templateEngine.addDialect(new SpringSecurityDialect());
		return templateEngine;
	}
	
	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(new HttpSessionEventPublisher());
    }
	
	@Bean
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> getHttpSessionEventPublisher() {
	    return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	}
}
