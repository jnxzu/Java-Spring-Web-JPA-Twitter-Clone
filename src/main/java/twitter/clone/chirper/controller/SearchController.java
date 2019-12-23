package twitter.clone.chirper.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import twitter.clone.chirper.domain.CurrentUser;
import twitter.clone.chirper.domain.Message;
import twitter.clone.chirper.domain.UserState;
import twitter.clone.chirper.service.MessageManager;

@Controller
public class SearchController {
    @Autowired
    @Qualifier("us")
    private UserState us;
    @Autowired
    @Qualifier("cu")
    private CurrentUser cu;

    @Autowired
    MessageManager mm;

    @GetMapping("search")
    public String search(@RequestParam("phrase") String phrase, final Model model) {
        if (us.isLogged()) {
            model.addAttribute("searchphrase", phrase);
            model.addAttribute("cuid", cu.getCurrent().getId());
            model.addAttribute("admin", us.isAdmin());
            List<Message> msgs = mm.findByContentIgnoreCaseContaining(phrase);
            Collections.reverse(msgs);
            model.addAttribute("messages", msgs);
            model.addAttribute("notFound", msgs.isEmpty());
            return "search-chirps";
        }
        return "redirect:/";
    }
}