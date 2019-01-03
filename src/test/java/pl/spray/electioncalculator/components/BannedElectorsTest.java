package pl.spray.electioncalculator.components;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.spray.electioncalculator.exceptions.ExternalServerErrorException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:additional.properties")
public class BannedElectorsTest {

    @Value("${fp.candidates.url}")
    private String url;

    @Value("${fp.invalid.url}")
    private String invalidUrl;

    @Autowired
    BannedElectorsComponent bannedElectors;

    @Test
    public void getJSONFromServer() throws ExternalServerErrorException {
        Assert.assertNotEquals(bannedElectors.getJSONFromExternalServer(url), null);
    }

    @Test(expected = ExternalServerErrorException.class)
    public void getJSONFromServerThrowsException() throws ExternalServerErrorException {
        bannedElectors.getJSONFromExternalServer(invalidUrl);
    }
}
