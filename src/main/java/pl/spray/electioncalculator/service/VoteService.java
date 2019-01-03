package pl.spray.electioncalculator.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.spray.electioncalculator.dao.CandidatesDAO;
import pl.spray.electioncalculator.dao.ResultsDAO;
import pl.spray.electioncalculator.dao.VoteCardDAO;
import pl.spray.electioncalculator.model.Candidates;
import pl.spray.electioncalculator.model.Results;
import pl.spray.electioncalculator.model.VoteCard;
import pl.spray.electioncalculator.model.VoteCardView;

@Service
public class VoteService {

	@Autowired
	VoteCardDAO voteCardDAO;

	@Autowired
	CandidatesDAO candidatesDAO;
	
	@Autowired
	ResultsDAO resultsDAO;

	public void sendVote(String username) {
		VoteCard card = getVoteCard(username);

		if(card.getVote().size() == 1){
			card.setValid(true);

			Candidates votedFor = card.getVote().get(0);

			Results voteCandidate = resultsDAO.findByName(votedFor.getName());
			Results voteParty = resultsDAO.findByPartyAndNameIsNull(votedFor.getParty());

			voteCandidate.vote();
			voteParty.vote();

			resultsDAO.save(voteCandidate);
			resultsDAO.save(voteParty);
		} else {
			card.setValid(false);
		}
		card.setVoted(true);
		voteCardDAO.save(card);
	}

	public void setConfirm(String username, Boolean value) {
		VoteCard card = getVoteCard(username);
		card.setConfirmed(value);
		voteCardDAO.save(card);
	}

	public Boolean voteFor(String username, Long id) {

		VoteCard card = getVoteCard(username);
		Candidates candidates = candidatesDAO.findById(id).get();
		if (card.getVote().contains(candidates)) {
			return false;
		}
		card.getVote().add(candidates);
		voteCardDAO.save(card);
		return true;
	}

	public Boolean unvoteFor(String username, Long id) {

		VoteCard card = getVoteCard(username);
		Candidates candidates = candidatesDAO.findById(id).get();
		if (!card.getVote().contains(candidates)) {
			return false;
		}
		card.getVote().remove(candidates);
		voteCardDAO.save(card);
		return true;
	}

	public List<VoteCardView> getVoteCardViews(String username) {

		List<Candidates> candidates = getVoteCard(username).getVote();
		List<VoteCardView> voteCardView = new ArrayList<>();

		candidatesDAO.findAll().stream().forEach(candidate -> {
			boolean vote;
			if(candidates.contains(candidate))
				vote = true;
			else
				vote = false;

			voteCardView.add(new VoteCardView(candidate.getName(), candidate.getParty(), vote , candidate.getId()));
		});
		return voteCardView;
	}

	public Boolean getConfirmedValue(String username) {
		return getVoteCard(username).getConfirmed();
	}

	public Boolean getVotedValue(String username) {
		return getVoteCard(username).getVoted();
	}

	private VoteCard getVoteCard(String username) {

		if (voteCardDAO.existsByUsername(username)) {
			return voteCardDAO.findByUsername(username);
		}
		VoteCard card = new VoteCard(username);
		voteCardDAO.save(card);
		return card;
	}
}
