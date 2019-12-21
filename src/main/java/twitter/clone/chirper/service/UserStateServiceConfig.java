package twitter.clone.chirper.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import twitter.clone.chirper.domain.UserState;

@Configuration
public class UserStateServiceConfig {

    @Bean
    public UserState us() {
        UserState us = new UserState();
        us.setAdmin(false);
        us.setLogged(false);
        return us;
    }

}
