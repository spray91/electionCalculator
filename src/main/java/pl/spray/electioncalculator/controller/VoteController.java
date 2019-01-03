package pl.spray.electioncalculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.spray.electioncalculator.exceptions.UnexpectedActionException;
import pl.spray.electioncalculator.model.Identity;
import pl.spray.electioncalculator.service.UserService;
import pl.spray.electioncalculator.service.VoteService;

@Controller
@RequestMapping(value = "/vote")
public class VoteController {

	@Autowired
	UserService userService;

	@Autowired
	VoteService voteService;

	@GetMapping("/")
	public String getVote(Model model, Identity identity) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		boolean confirmed = voteService.getConfirmedValue(auth.getName());
		model.addAttribute("confirmed", confirmed);
		model.addAttribute("voted", voteService.getVotedValue(auth.getName()));

		if (!confirmed) {
			model.addAttribute("card", voteService.getVoteCardViews(auth.getName()));
		}
		return "vote";
	}

	@GetMapping("/votefor/{id}")
	public String getVoteFor(@PathVariable("id") Long id, Model model) throws UnexpectedActionException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!voteService.getConfirmedValue(auth.getName()) && !voteService.getVotedValue(auth.getName())
				&& voteService.voteFor(auth.getName(), id)) {
			return "redirect:/vote/";
		}
		model.addAttribute("code", "111");
		return "error";
	}
	
	@GetMapping("/unvotefor/{id}")
	public String getUnvoteFor(@PathVariable("id") Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!voteService.getConfirmedValue(auth.getName()) && !voteService.getVotedValue(auth.getName())
				&& voteService.unvoteFor(auth.getName(), id)) {
			return "redirect:/vote/";
		}
		return "error";
	}

	@GetMapping("/send")
	public String getVoteSend() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (voteService.getConfirmedValue(auth.getName()) && !voteService.getVotedValue(auth.getName())) {
			voteService.sendVote(auth.getName());
			return "redirect:/results";
		}
		return "error";
	}

	@GetMapping("/confirm")
	public String getVoteConfirm() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!voteService.getConfirmedValue(auth.getName()) && !voteService.getVotedValue(auth.getName())) {
			voteService.setConfirm(auth.getName(), true);
			return "redirect:/vote/";
		}
		return "error";
	}

	@GetMapping("/modify")
	public String getVoteModify() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (voteService.getConfirmedValue(auth.getName()) && !voteService.getVotedValue(auth.getName())) {
			voteService.setConfirm(auth.getName(), false);
			return "redirect:/vote/";
		}
		return "error";
	}
}
