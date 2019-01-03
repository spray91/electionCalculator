package pl.spray.electioncalculator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.spray.electioncalculator.model.Authorities;

@Repository
public interface AuthoritiesDAO extends JpaRepository<Authorities, Long> {
	
}
