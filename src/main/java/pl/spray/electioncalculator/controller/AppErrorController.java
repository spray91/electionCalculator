package pl.spray.electioncalculator.controller;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class AppErrorController implements ErrorController {
	private final static String ERROR_PATH = "/error";
	
	@RequestMapping("/error")
    public String error(Model model, HttpServletRequest httpRequest) {
		Integer statusCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
		Exception exception = (Exception) httpRequest.getAttribute("javax.servlet.error.exception");
		model.addAttribute("code",statusCode);
		model.addAttribute("message", exception.getLocalizedMessage());
        return "error";
    }

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}
