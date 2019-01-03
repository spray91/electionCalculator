package pl.spray.electioncalculator.components;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.spray.electioncalculator.exceptions.ExternalServerErrorException;
import pl.spray.electioncalculator.model.Candidates;

@Slf4j
@Service
public class CandidatesComponent implements GetDataFromExternalServer{

	private final static String JSON_OBJECT_NAME = "candidates";
	private final static String JSON_ARRAY_NAME = "candidate";
	private final static String JSON_FIELD1_NAME = "name";
	private final static String JSON_FIELD2_NAME = "party";

	@Value("${fp.candidates.url}")
	private String candidatesUrl;

	public ArrayList<Candidates> getCandidatesFromExternalServer() throws ExternalServerErrorException {
		return createCandidatesArray(getCandidates(candidatesUrl));
	}

	private JSONObject getCandidates(String url) throws ExternalServerErrorException {
		log.info("Connecting to external server: " + url);
		return getJSONFromExternalServer(url);
	}

	private ArrayList<Candidates> createCandidatesArray(JSONObject jsonObject){

		ArrayList<Candidates> candidates = new ArrayList<>();

		JSONArray jArray = jsonObject.getJSONObject(JSON_OBJECT_NAME).getJSONArray(JSON_ARRAY_NAME);

		if (jArray != null) {
			for (int i = 0; i < jArray.length(); i++) {
				candidates.add(new Candidates(jArray.getJSONObject(i).getString(JSON_FIELD1_NAME),
						jArray.getJSONObject(i).getString(JSON_FIELD2_NAME)));
			}
		}
		return candidates;
	}
}
