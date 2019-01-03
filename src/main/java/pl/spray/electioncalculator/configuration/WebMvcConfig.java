package pl.spray.electioncalculator.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.spray.electioncalculator.service.UserService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new CustomInterceptor(userService));
    }
}
