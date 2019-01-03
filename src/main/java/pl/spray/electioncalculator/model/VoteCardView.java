package pl.spray.electioncalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteCardView {
	private String name;
	
	private String party;
	
	private Boolean vote;
	
	private Long id;
}
