package pl.spray.electioncalculator.service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.spray.electioncalculator.dao.AuthoritiesDAO;
import pl.spray.electioncalculator.dao.UsersDAO;
import pl.spray.electioncalculator.model.Authorities;
import pl.spray.electioncalculator.model.Users;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class RegistrationService {
	
	@Autowired
	UsersDAO usersDao;
	
	@Autowired
	AuthoritiesDAO authDao;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	public Boolean registerUser(Users elector) {
		
		if (usersDao.existsByUsername(elector.getUsername())) {
			return false;
		} 
		
		elector.setPassword(passwordEncoder.encode(elector.getPassword()));
		elector.setPesel(passwordEncoder.encode(elector.getPesel()));
		
		usersDao.save(elector);
		authDao.save(new Authorities(elector.getUsername(), "USER"));
		
		log.info("User " + elector.getUsername() + " has been registered");
		return true;
	}



}
