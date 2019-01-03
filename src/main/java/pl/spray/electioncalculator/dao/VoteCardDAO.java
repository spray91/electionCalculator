package pl.spray.electioncalculator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.spray.electioncalculator.model.VoteCard;

@Repository
public interface VoteCardDAO extends JpaRepository<VoteCard, String> {
	
	VoteCard findByUsername(String username);
	
	boolean existsByUsername(String username);
	
	Integer countByConfirmedAndVotedAndValid(Boolean confirmed, Boolean voted, Boolean valid);
}
