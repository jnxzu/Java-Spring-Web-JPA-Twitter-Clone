package twitter.clone.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import twitter.clone.chirper.domain.Message;
import twitter.clone.chirper.domain.UserState;
import twitter.clone.chirper.service.MessageManager;

@Controller
public class HomeController {
    @Autowired
    @Qualifier("us")
    private UserState us;

    @Autowired
    MessageManager mm;

    @GetMapping("home")
    public String home(final Model model) {
        if (us.isLogged()) {
            model.addAttribute("newmsg", new Message());
            model.addAttribute("messages", mm.findAll());
            return "home";
        }
        return "redirect:/";
    }
}