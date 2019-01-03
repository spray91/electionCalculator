package pl.spray.electioncalculator.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Identity {
	
	@Pattern(regexp = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*", message="Name: Only letters are allowed")
	@Size(min=1, message="Please fill you Name.")
	private String name;
	
	@Pattern(regexp = "[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*", message="Surname: Only letters are allowed")
	@Size(min=1, message="Please fill you Surname.")
	private String surname;
	
	@Pattern(regexp="^\\d{11}$", message="PESEL number is not valid!")
	private String pesel;

}
