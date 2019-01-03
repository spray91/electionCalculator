package pl.spray.electioncalculator.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import pl.spray.electioncalculator.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomInterceptor implements HandlerInterceptor {


    UserService userService;

    @Autowired
    public CustomInterceptor(UserService userService){
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        if(request.getRequestURI().startsWith("/vote/")){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            if (!userService.checkIdentity(auth.getName())) {
                response.sendRedirect("/confirm");
                return false;
            }
            if(!userService.canUserVote(auth.getName())) {
                response.sendRedirect("/blocked");
                return false;
            }

        }
        return true;
    }
}
