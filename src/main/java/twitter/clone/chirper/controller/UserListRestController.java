package twitter.clone.chirper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import twitter.clone.chirper.domain.ChirperUser;
import twitter.clone.chirper.domain.UserState;
import twitter.clone.chirper.service.UserManager;

@RestController
public class UserListRestController {
    @Autowired
    UserManager um;
    @Autowired
    @Qualifier("us")
    private UserState us;

    @GetMapping("userlist")
    public List<ChirperUser> showAllUsers() {
        if (us.isAdmin())
            return um.findAll();
        else
            return null;
    }
}