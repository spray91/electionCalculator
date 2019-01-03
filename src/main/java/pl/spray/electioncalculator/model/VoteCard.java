package pl.spray.electioncalculator.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import lombok.Data;

@Data
@Entity
@NoArgsConstructor
public class VoteCard {
	@Id
	private String username;

	@ElementCollection
	@CollectionTable(name = "votes")
	private List<Candidates> vote = new ArrayList<>();
	
	private Boolean confirmed;
	
	private Boolean voted;
	
	private Boolean valid;
	
	public VoteCard(String username) {
		this.username = username;
		this.confirmed = false;
		this.voted = false;
		this.valid = true;
	}
	
	public VoteCard(Boolean value) {
		this.username = RandomStringUtils.random(10, true, false);
		this.confirmed = true;
		this.voted = true;
		this.valid = value;
	}

}
