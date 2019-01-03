package pl.spray.electioncalculator.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pl.spray.electioncalculator.dao.AuthoritiesDAO;
import pl.spray.electioncalculator.dao.UsersDAO;
import pl.spray.electioncalculator.model.Authorities;
import pl.spray.electioncalculator.model.Users;
import pl.spray.electioncalculator.service.UserService;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UsersDAO usersDao;

    @Autowired
    AuthoritiesDAO authoritiesDao;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    @Before
    public void setUp(){
        Users testUser = new Users("User3", new BCryptPasswordEncoder().encode("password1"));
        String pesel = encoder.encode("12345678901");
        testUser.setPesel(pesel);
        usersDao.save(testUser);
        authoritiesDao.save(new Authorities("User3", "USER"));
    }

    @Test
    public void checkIfPeselAlreadyExists(){
        Assert.assertEquals(true, userService.checkPesel("12345678901"));
    }

    @After
    public void cleanUp(){
        usersDao.deleteAll();
    }
}
