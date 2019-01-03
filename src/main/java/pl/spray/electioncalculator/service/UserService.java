package pl.spray.electioncalculator.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.spray.electioncalculator.components.BannedElectorsComponent;
import pl.spray.electioncalculator.dao.UsersDAO;
import pl.spray.electioncalculator.exceptions.ExternalServerErrorException;
import pl.spray.electioncalculator.model.Identity;
import pl.spray.electioncalculator.model.Users;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	UsersDAO usersDAO;
	
	@Autowired
	BannedElectorsComponent bannedElectorsComponent;

	public boolean checkIdentity(String username) {

		Users user = usersDAO.findByUsername(username);
		if (user.getPesel() == null)
			return false;

		return true;
	}
	
	public void confirmUserIdentity(Identity identity, String username) throws ExternalServerErrorException {
		
		Users user = usersDAO.findByUsername(username);
		
		user.setName(identity.getName());
		user.setSurname(identity.getSurname());
		user.setPesel(new BCryptPasswordEncoder().encode(identity.getPesel()));
		user.setBirthDate(getBirthDateFromPesel(identity.getPesel()));
		user.setCanVote(bannedElectorsComponent.canUserVote(identity.getPesel(), user.getBirthDate()));
		
		usersDAO.save(user);
	}
	
	public Boolean canUserVote(String username) {
		return usersDAO.findByUsername(username).getCanVote();
	}
	
	public Boolean checkPesel(String pesel) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		AtomicBoolean result = new AtomicBoolean(false);
		usersDAO.findAll().stream().forEach(c -> {
			if(encoder.matches(pesel, c.getPesel())) {
				result.set(true);
			}			
		});
		return result.get();
	}

	public String getBlockedMessage(String username){
		Users user = usersDAO.findByUsername(username);

		if(user.getBirthDate().plusYears(18).isAfter(LocalDate.now()))
			return "You are underage and you cannot vote.";
		else
			return "You are on the list of people without election rights.";
	}

	private LocalDate getBirthDateFromPesel(String pesel){
		String date = pesel.substring(0, 6);
		List<String> x = Arrays.asList( date.split("(?<=\\G..)") );
		Integer year;
		Integer month = Integer.parseInt(x.get(1));
		Integer day = Integer.parseInt(x.get(2));

		if(month < 13){
			year = Integer.parseInt("19"+x.get(0));
		} else if(month < 33){
			year = Integer.parseInt("20"+x.get(0));
			month = month - 20;
		} else if(month < 53){
			year = Integer.parseInt("21"+x.get(0));
			month = month - 40;
		} else if(month < 73){
			year = Integer.parseInt("22"+x.get(0));
			month = month - 60;
		} else {
			year = Integer.parseInt("18"+x.get(0));
			month = month - 80;
		}

		return new LocalDate(year, month, day);
	}
}
