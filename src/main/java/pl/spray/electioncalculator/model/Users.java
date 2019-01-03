package pl.spray.electioncalculator.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name="users")
public class Users {
	
	@Id
	@NotBlank
	@Size(min=4, message="Username is too short. Minimum length is 4")
	private String username;
	
	@NotBlank
	@Size(min=8, message="Password is too short. Minimum length is 8")
	private String password;

	private boolean enabled;
	
	private String name;
	
	private String surname;
	
	private String pesel;
	
	private Boolean canVote;

	private LocalDate birthDate;
	
	
	public Users(String username, String password) {
		this.username = username;
		this.password = password;
		this.enabled = true;
	}

}
