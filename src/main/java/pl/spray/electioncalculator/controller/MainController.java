package pl.spray.electioncalculator.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import pl.spray.electioncalculator.model.Users;
import pl.spray.electioncalculator.service.RegistrationService;

import java.util.ArrayList;

@Slf4j
@Controller
public class MainController {

	public final static String USER_CREATED = "User %s has been created. You can Sign In right now.";
	public final static String USER_EXIST = "User %s already exists.";
	
	@Autowired
	RegistrationService registrationService;
	
	@RequestMapping("/")
    public String index() {
        return "index";
    }
	
	@RequestMapping("/login")
    public String login() {
        return "login";
    }
	
	@GetMapping("/register")
    public String getRegister(Users elector, Model model) {
		model.addAttribute("elector", elector);
        return "register";
    }
	
	@PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("elector") Users elector, BindingResult result, Model model) {		
		if (result.hasErrors()) {
			ArrayList<String> errorMessage = new ArrayList<String>();
			result.getAllErrors().stream().forEach(s -> {
				errorMessage.add(s.getDefaultMessage());
			});
			model.addAttribute("message", errorMessage);
			return "register";
		}
		if(registrationService.registerUser(elector)) {
			model.addAttribute("message", String.format(USER_CREATED, elector.getUsername()));
			return "login";
		} else {
			model.addAttribute("message", String.format(USER_EXIST, elector.getUsername()));
			return "register";
		}
    }
}
