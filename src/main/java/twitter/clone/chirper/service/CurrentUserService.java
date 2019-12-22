package twitter.clone.chirper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import twitter.clone.chirper.domain.CurrentUser;

@Service
public class CurrentUserService {

    @Autowired
    @Qualifier("cu")
    CurrentUser cu;

    CurrentUser getCu() {
        return cu;
    }

}
