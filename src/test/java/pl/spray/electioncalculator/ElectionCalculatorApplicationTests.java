package pl.spray.electioncalculator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.spray.electioncalculator.dao.AuthoritiesDAO;
import pl.spray.electioncalculator.dao.UsersDAO;
import pl.spray.electioncalculator.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElectionCalculatorApplicationTests {

	@Test
	public void contextLoads() {
	}

}
