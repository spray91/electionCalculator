package pl.spray.electioncalculator.components;

import java.util.ArrayList;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.spray.electioncalculator.exceptions.ExternalServerErrorException;

@Slf4j
@Component
public class BannedElectorsComponent implements GetDataFromExternalServer {

	private final static String JSON_OBJECT_NAME = "disallowed";
	private final static String JSON_ARRAY_NAME = "person";
	private final static String JSON_FIELD_NAME = "pesel";

	@Value("${fp.banned.url}")
	private String serverUrl;

	public boolean canUserVote(String pesel, LocalDate birthDate) throws ExternalServerErrorException {

		ArrayList<String> bannedPesels = createBannedElectosArray(getBannedElectors(serverUrl));
		
		if (bannedPesels.contains(pesel) || birthDate.plusYears(18).isAfter(LocalDate.now())) {
			return false;
		}
		return true;
	}

	private ArrayList<String> createBannedElectosArray(JSONObject jsonObject){
		ArrayList<String> bannedElectorsArray = new ArrayList<>();

		JSONArray jsonArray = jsonObject.getJSONObject(JSON_OBJECT_NAME).getJSONArray(JSON_ARRAY_NAME);
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.length(); i++) {
				bannedElectorsArray.add(jsonArray.getJSONObject(i).getString(JSON_FIELD_NAME));
			}
		}
		return bannedElectorsArray;
	}

	private JSONObject getBannedElectors(String url) throws ExternalServerErrorException {
		log.info("Connecting to external server: " + url);
		return getJSONFromExternalServer(url);
	}
}
