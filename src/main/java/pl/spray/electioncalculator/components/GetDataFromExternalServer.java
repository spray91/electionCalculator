package pl.spray.electioncalculator.components;


import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.spray.electioncalculator.exceptions.ExternalServerErrorException;

@Component
public interface GetDataFromExternalServer {

    default JSONObject getJSONFromExternalServer(String url) throws ExternalServerErrorException {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;

        try{
            response = restTemplate.getForEntity(url, String.class);
        } catch (Exception ex){
            throw new ExternalServerErrorException(ex.getMessage(), ex.getCause());
        }

        if(!response.getStatusCode().equals(HttpStatus.OK)){
            throw new ExternalServerErrorException("Response from external server was empty.", null);
        }

        return new JSONObject(response.getBody());
    }
}
