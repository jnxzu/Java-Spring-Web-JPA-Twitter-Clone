package twitter.clone.chirper.controller;

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
import org.springframework.web.bind.annotation.RequestParam;

import twitter.clone.chirper.domain.ChirperUser;
import twitter.clone.chirper.domain.CurrentUser;
import twitter.clone.chirper.domain.Message;
import twitter.clone.chirper.domain.UserState;
import twitter.clone.chirper.service.MessageManager;
import twitter.clone.chirper.service.UserManager;

@Controller
public class ProfileController {
    @Autowired
    @Qualifier("us")
    private UserState us;
    @Autowired
    @Qualifier("cu")
    private CurrentUser cu;

    @Autowired
    MessageManager mm;
    @Autowired
    UserManager um;

    @GetMapping("profile")
    public String profile(@RequestParam("id") int id, final Model model) {
        if (us.isLogged()) {
            model.addAttribute("profileUser", um.findById(id));
            model.addAttribute("cuid", cu.getCurrent().getId());
            model.addAttribute("myProfile", cu.getCurrent().getId() == id);
            model.addAttribute("admin", us.isAdmin());
            List<Message> msgs = mm.findByAuthors_Id(id);
            Collections.reverse(msgs);
            model.addAttribute("messages", msgs);
            model.addAttribute("notFound", msgs.isEmpty());
            return "user-profile";
        }
        return "redirect:/";
    }

    @PostMapping("editUser")
    public String editUser(@Valid final ChirperUser user, Errors errors, final Model model, @RequestParam int id) {
        if (errors.hasErrors()) {
            model.addAttribute("profileUser", um.findById(id));
            model.addAttribute("cuid", cu.getCurrent().getId());
            model.addAttribute("myProfile", cu.getCurrent().getId() == id);
            model.addAttribute("admin", us.isAdmin());
            List<Message> msgs = mm.findByAuthors_Id(id);
            Collections.reverse(msgs);
            model.addAttribute("messages", msgs);
            model.addAttribute("notFound", msgs.isEmpty());
            // TODO FIX THIS ERROR BINDING
            return "user-profile-E";
        }
        updateUser(user, id);
        return "redirect:/profile?id=" + id;
    }

    public void updateUser(ChirperUser user, int id) {
        ChirperUser toEdit = um.getOneById(id);
        toEdit.setFirstName(user.getFirstName());
        toEdit.setNick(user.getNick());
        toEdit.setLastName(user.getLastName());
        toEdit.setEmail(user.getEmail());
        toEdit.setPassword(user.getPassword());
        um.save(toEdit);
    }

    @PostMapping("deleteUser")
    public String deleteUser(@RequestParam int id) {
        mm.deleteByAuthors_Id(id);
        um.deleteById(id);
        return "redirect:/home/logout";
    }
}