package pl.spray.electioncalculator.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.spray.electioncalculator.model.Candidates;

@Repository
public interface CandidatesDAO extends JpaRepository<Candidates, Long> {
	
	@Query("SELECT DISTINCT(c.party) from Candidates c")
	List<String> getDistinctParty();
}
