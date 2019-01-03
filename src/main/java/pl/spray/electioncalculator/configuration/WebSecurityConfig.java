package pl.spray.electioncalculator.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	 @Autowired 
	 private DataSource dataSource;
	  
	 @Override protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth
		 	.jdbcAuthentication()
		 	.dataSource(dataSource)
		 	.passwordEncoder(passwordEncoder())
		 	.usersByUsernameQuery("select username, password, enabled from users where username=?");
	 }
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
	        .authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/error").permitAll()
				.antMatchers("/results").permitAll()
				.antMatchers("/results/**").permitAll()
				.antMatchers("/h2/*").permitAll()
				.antMatchers("/console/*").permitAll()
				.anyRequest().authenticated()
	        .and()
	        	.formLogin().loginPage("/login").permitAll()
			.and()
	        	.logout().deleteCookies("JSESSIONID").permitAll();
	
		http.headers().frameOptions().disable();
		
		http.sessionManagement()
			.maximumSessions(1)
			.maxSessionsPreventsLogin(true);
			
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}