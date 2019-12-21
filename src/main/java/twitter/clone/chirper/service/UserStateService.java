package twitter.clone.chirper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import twitter.clone.chirper.domain.UserState;

@Service
public class UserStateService {

    @Autowired
    @Qualifier("us")
    UserState us;

    UserState getUs() {
        return us;
    }

}
