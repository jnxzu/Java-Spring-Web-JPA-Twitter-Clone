package twitter.clone.chirper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import twitter.clone.chirper.domain.CurrentUser;
import twitter.clone.chirper.domain.UserState;
import twitter.clone.chirper.service.UserManager;

@Controller
public class AdminPageController {
    @Autowired
    @Qualifier("us")
    private UserState us;
    @Autowired
    @Qualifier("cu")
    private CurrentUser cu;

    @Autowired
    UserManager um;

    @GetMapping("admin")
    public String admin(@RequestParam(required = false) String nick, final Model model) {
        if (us.isLogged() && us.isAdmin()) {
            if (nick == null || nick.isEmpty())
                model.addAttribute("users", um.findAll());
            else
                model.addAttribute("users", um.findByNickIgnoreCaseContaining(nick));
            model.addAttribute("cuid", cu.getCurrent().getId());
            model.addAttribute("admin", us.isAdmin());
            return "admin-page";
        } else if (us.isLogged() && !us.isAdmin()) {
            return "redirect:/home";
        } else {
            return "redirect:/";
        }
    }
}