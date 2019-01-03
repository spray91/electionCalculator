package pl.spray.electioncalculator.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.spray.electioncalculator.model.Results;

public interface ResultsDAO extends JpaRepository<Results, Integer> {
	
	List<Results> findByNameIsNull();
	
	List<Results> findByNameIsNotNull();

	Results findByName(String name);
	
	Results findByPartyAndNameIsNull(String party);
}
