package pl.spray.electioncalculator.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import pl.spray.electioncalculator.dao.ResultsDAO;
import pl.spray.electioncalculator.model.Results;
import pl.spray.electioncalculator.service.ResultsService;

@Controller
public class ResultsController {

	public final static String HEADER_KEY = "Content-Disposition";
	public final static String HEADER_VALUE = "attachment; filename=\"results.csv\"";

	@Autowired
	ResultsService resultService;

	@Autowired
	ResultsDAO resultsDAO;

	@RequestMapping("/results")
	public String main(Model model) {
		model.addAttribute("party", resultService.getPartyVotes());
		model.addAttribute("candidates", resultService.getCandidatesVotes());
		model.addAttribute("validvotes", resultService.getValidVotes());
		model.addAttribute("invalidvotes", resultService.getInvalidVotes());
		return "results";
	}

	@RequestMapping(value = "/results/csv", produces = "text/csv; charset=utf-8")
	public void getCsv(HttpServletResponse response) throws IOException {

		response.setHeader(HEADER_KEY, HEADER_VALUE);
		response.setContentType("text/csv; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		List<Results> results = resultsDAO.findAll();

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] header = { "name", "party", "votes" };

		csvWriter.writeHeader(header);

		for (Results result : results) {
			csvWriter.write(result, header);
		}

		csvWriter.write(new Results("Invalid votes", resultService.getInvalidVotes()), header);
		csvWriter.write(new Results("Valid votes", resultService.getValidVotes()), header);

		csvWriter.close();
	}
}
