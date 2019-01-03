package pl.spray.electioncalculator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.spray.electioncalculator.dao.ResultsDAO;
import pl.spray.electioncalculator.dao.VoteCardDAO;
import pl.spray.electioncalculator.model.Results;

@Service
public class ResultsService {

	@Autowired
	ResultsDAO resultsDAO;

	@Autowired
	VoteCardDAO voteCardDAO;

	public List<Results> getPartyVotes() {
		return resultsDAO.findByNameIsNull();
	}

	public List<Results> getCandidatesVotes() {
		return resultsDAO.findByNameIsNotNull();
	}

	public Integer getValidVotes() {
		return voteCardDAO.countByConfirmedAndVotedAndValid(true, true, true);
	}

	public Integer getInvalidVotes() {
		return voteCardDAO.countByConfirmedAndVotedAndValid(true, true, false);
	}

}
