package pl.spray.electioncalculator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.spray.electioncalculator.components.CandidatesComponent;
import pl.spray.electioncalculator.dao.AuthoritiesDAO;
import pl.spray.electioncalculator.dao.CandidatesDAO;
import pl.spray.electioncalculator.dao.ResultsDAO;
import pl.spray.electioncalculator.dao.UsersDAO;
import pl.spray.electioncalculator.dao.VoteCardDAO;
import pl.spray.electioncalculator.model.Authorities;
import pl.spray.electioncalculator.model.Results;
import pl.spray.electioncalculator.model.Users;
import pl.spray.electioncalculator.model.VoteCard;

@Slf4j
@Component
public class TempDataLoader implements CommandLineRunner {

    private UsersDAO usersDao;
    private AuthoritiesDAO authoritiesDao;
    private CandidatesComponent candidatesComponent;
    private CandidatesDAO candidatesDao;
    private ResultsDAO resultsDao;
    private VoteCardDAO voteCardDao;

    @Autowired
    public TempDataLoader(UsersDAO usersDao, AuthoritiesDAO authoritiesDao, CandidatesComponent candidatesComponent,
                          CandidatesDAO candidatesDao, ResultsDAO resultsDao, VoteCardDAO voteCardDao){
        this.usersDao = usersDao;
        this.authoritiesDao = authoritiesDao;
        this.candidatesComponent = candidatesComponent;
        this.candidatesDao = candidatesDao;
        this.resultsDao = resultsDao;
        this.voteCardDao = voteCardDao;
    }

    @Override
    public void run(String... strings) throws Exception{
        log.info("##################################");

        log.info("LOADING OF TEMP DATA STARTED");
        log.info("CANDIDATES");
        candidatesComponent.getCandidatesFromExternalServer().stream().forEach(c -> {
            candidatesDao.save(c);
            resultsDao.save(new Results(c.getName(), c.getParty()));
            log.info(c.toString());
        });
        ;
        log.info("PARTY");
        candidatesDao.getDistinctParty().stream().forEach(c -> {
            resultsDao.save(new Results(c));
            log.info(c.toString());
        });

        log.info("VOTECARDS");
        for (int i = 0; i < 30; i++) {
            voteCardDao.save(new VoteCard(true));
        }
        for (int i = 0; i < 5; i++) {
            voteCardDao.save(new VoteCard(false));
        }
        log.info("RESULTS");
        resultsDao.findAll().stream().forEach( c -> {log.info(c.toString());});

        for (int i = 1; i < 5; i++) {
            Results result = resultsDao.findById(4*i).get();
            log.info(result.toString());
            result.setVotes(i * i);
            Results party = resultsDao.findByPartyAndNameIsNull(result.getParty());
            party.setVotes(party.getVotes() + i * i);
            resultsDao.save(party);
            resultsDao.save(result);
        }

        usersDao.save(new Users("User1", new BCryptPasswordEncoder().encode("password1")));
        authoritiesDao.save(new Authorities("User1", "USER"));
        usersDao.save(new Users("User2", new BCryptPasswordEncoder().encode("password2")));
        authoritiesDao.save(new Authorities("User2", "USER"));

        log.info("LOADING OF TEMP DATA FINISHED");
        log.info("##################################");
    }


}
