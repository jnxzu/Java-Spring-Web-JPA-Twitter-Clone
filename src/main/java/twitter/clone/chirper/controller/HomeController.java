package twitter.clone.chirper.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import twitter.clone.chirper.domain.CurrentUser;
import twitter.clone.chirper.domain.Message;
import twitter.clone.chirper.domain.UserState;
import twitter.clone.chirper.service.MessageManager;

@Controller
public class HomeController {
    @Autowired
    @Qualifier("us")
    private UserState us;
    @Autowired
    @Qualifier("cu")
    private CurrentUser cu;

    @Autowired
    MessageManager mm;

    @GetMapping("home")
    public String home(final Model model) {
        if (us.isLogged()) {
            model.addAttribute("cuid", cu.getCurrent().getId());
            model.addAttribute("admin", us.isAdmin());
            model.addAttribute("message", new Message());
            List<Message> allMsgs = mm.findAll();
            Collections.reverse(allMsgs);
            model.addAttribute("messages", allMsgs);
            return "home";
        }
        return "redirect:/";
    }

    @PostMapping("home")
    public String newmsg(final Model model, @Valid final Message msg, Errors errors) {
        if (!errors.hasErrors()) {
            msg.setAuthors(Arrays.asList(cu.getCurrent()));
            mm.save(msg);
            model.addAttribute("message", new Message());
            List<Message> allMsgs = mm.findAll();
            Collections.reverse(allMsgs);
            model.addAttribute("messages", allMsgs);
            return "redirect:/home";
        }
        List<Message> allMsgs = mm.findAll();
        Collections.reverse(allMsgs);
        model.addAttribute("messages", allMsgs);
        return "home";
    }

    @GetMapping("home/logout")
    public String logout() {
        us.setAdmin(false);
        us.setLogged(false);
        cu.setCurrent(null);
        return "redirect:/";
    }

    // TODO ADVANCED POST
    // TODO MSG EDIT
}