package twitter.clone.chirper.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import twitter.clone.chirper.domain.CurrentUser;

@Configuration
public class CurrentUserServiceConfig {

    @Bean
    public CurrentUser cu() {
        return new CurrentUser();
    }

}
