package pl.spray.electioncalculator.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import pl.spray.electioncalculator.exceptions.ExternalServerErrorException;
import pl.spray.electioncalculator.model.Identity;
import pl.spray.electioncalculator.service.UserService;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class IdentityController {

	@Autowired
	UserService userService;

	@GetMapping("/confirm")
	public String getConfirm() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (userService.checkIdentity(auth.getName())) {
			return "redirect:/vote/";
		}
		return "confirm";
	}


	@GetMapping("/blocked")
	public String getBlocked(Model model){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("message",userService.getBlockedMessage(auth.getName()));
		return "blocked";
	}

	@PostMapping("/confirm")
	public String postConfirm(@Valid @ModelAttribute("identity") Identity identity, BindingResult result, Model model) throws ExternalServerErrorException {
		if (result.hasErrors() || !isValidPesel(identity.getPesel()) || userService.checkPesel(identity.getPesel())) {
			ArrayList<String> errorMessage = new ArrayList<String>();
			result.getAllErrors().stream().forEach(s -> {
				errorMessage.add(s.getDefaultMessage());
			});

			if(!isValidPesel(identity.getPesel())) {
				errorMessage.add("PESEL number is not valid!");
			}
			if(userService.checkPesel(identity.getPesel())) {
				errorMessage.add("User with this PESEL number already exist in system");
			}
			model.addAttribute("errorMessage", errorMessage);
			return "confirm";
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		userService.confirmUserIdentity(identity, auth.getName());
		return "redirect:/vote/";
	}

	private Boolean isValidPesel(String pesel) {

		Pattern p = Pattern.compile("^\\d{11}$");
		Matcher m = p.matcher(pesel);

		if (m.find()) {
			int[] factor = { 1, 3, 7, 9, 1, 3, 7, 9, 1, 3 };
			int sum = 0;
			for (int i = 0; i < 10; i++)
				sum += Integer.parseInt(pesel.substring(i, i + 1)) * factor[i];

			if (Integer.parseInt(pesel.substring(10, 11)) == (Math.abs(sum % 10 - 10)))
				return true;
		}
		return false;
	}
}
