package pl.spray.electioncalculator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.spray.electioncalculator.model.Users;

@Repository
public interface UsersDAO extends JpaRepository<Users, String> {
	
	Boolean existsByUsername(String username);
	
	Users findByUsername(String username);
}
