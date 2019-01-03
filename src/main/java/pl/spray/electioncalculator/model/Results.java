package pl.spray.electioncalculator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Results {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String name;
	
	private String party;
	
	private Integer votes;
	
	public void vote() {
		this.votes++;
	}
	
	public Results(String name, String party) {
		this.name = name;
		this.party = party;
		this.votes = 0;
	}
	
	public Results(String party) {
		this.party = party;
		this.votes = 0;
	}
	
	public Results(String party, Integer votes) {
		this.party = party;
		this.votes = votes;
	}
}
