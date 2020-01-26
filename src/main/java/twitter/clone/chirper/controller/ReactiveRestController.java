package twitter.clone.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twitter.clone.chirper.domain.ChirperUser;
import twitter.clone.chirper.domain.Message;
import twitter.clone.chirper.domain.UserState;
import twitter.clone.chirper.service.ReactiveMessageDao;
import twitter.clone.chirper.service.ReactiveUserDao;

@RestController
public class ReactiveRestController {
    ReactiveUserDao udao;
    ReactiveMessageDao mdao;
    @Autowired
    @Qualifier("us")
    private UserState us;

    public ReactiveRestController() {
        this.udao = new ReactiveUserDao();
        this.mdao = new ReactiveMessageDao();
    }

    // USERS

    @GetMapping("userlist")
    public Flux<ChirperUser> showAllUsers() {
        if (us.isAdmin())
            return udao.findAll();
        else
            return null;
    }

    @GetMapping("user")
    public Mono<ChirperUser> showUser(@RequestParam("id") int id) {
        if (us.isAdmin())
            return udao.findOneById(id);
        else
            return null;
    }

    @GetMapping("adduser")
    public Mono<ChirperUser> addUser(@RequestParam("fname") String fname, @RequestParam("lname") String lname,
            @RequestParam("nick") String nick, @RequestParam("mail") String mail, @RequestParam("pass") String pass) {
        if (us.isAdmin()) {
            ChirperUser newUser = new ChirperUser();
            newUser.setFirstName(fname);
            newUser.setLastName(lname);
            newUser.setNick(nick);
            newUser.setEmail(mail);
            newUser.setPassword(pass);
            return udao.addUser(newUser);
        } else
            return null;
    }

    @GetMapping("deleteuser")
    public Mono<ChirperUser> deleteUser(@RequestParam("id") int id) {
        if (us.isAdmin())
            return udao.deleteUser(id);
        else
            return null;
    }

    @GetMapping("changenick")
    public Mono<ChirperUser> changeNickname(@RequestParam("id") int id, @RequestParam("nick") String nick) {
        if (us.isAdmin())
            return udao.changeNickname(id, nick);
        else
            return null;
    }

    // MESSAGES

    @GetMapping("messagelist")
    public Flux<Message> showAllMessages() {
        if (us.isAdmin())
            return mdao.findAll();
        else
            return null;
    }

    @GetMapping("message")
    public Mono<Message> showMessage(@RequestParam("id") int id) {
        if (us.isAdmin())
            return mdao.findOneById(id);
        else
            return null;
    }

    @GetMapping("deletemessage")
    public Mono<Message> deleteMessage(@RequestParam("id") int id) {
        if (us.isAdmin())
            return mdao.deleteMessage(id);
        else
            return null;
    }

    @GetMapping("editmessage")
    public Mono<Message> editMessage(@RequestParam("id") int id, @RequestParam("content") String content) {
        if (us.isAdmin())
            return mdao.editMessage(id, content);
        else
            return null;
    }
}