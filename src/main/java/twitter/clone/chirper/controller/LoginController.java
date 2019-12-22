package twitter.clone.chirper.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import twitter.clone.chirper.domain.ChirperLogin;
import twitter.clone.chirper.domain.ChirperUser;
import twitter.clone.chirper.domain.CurrentUser;
import twitter.clone.chirper.domain.UserState;
import twitter.clone.chirper.service.UserManager;

@Controller
public class LoginController {
    @Autowired
    @Qualifier("us")
    private UserState us;
    @Autowired
    @Qualifier("cu")
    private CurrentUser cu;

    @Autowired
    UserManager um;

    @GetMapping("/")
    public String start(@ModelAttribute("chirperuser") ChirperUser chirperuser,
            @ModelAttribute("chirperlogin") ChirperLogin chirperlogin) {
        if (us.isLogged())
            return "redirect:/home";
        return "loginS";
    }

    @PostMapping("login")
    public String login(@Valid @ModelAttribute("chirperlogin") ChirperLogin chirperlogin, BindingResult result,
            final Model model) {
        if (loginValidation(chirperlogin).equals("ok")) {
            us.setLogged(true);
            if (chirperlogin.getNickname().equals("administrator"))
                us.setAdmin(true);
            cu.setCurrent(um.findByNick(chirperlogin.getNickname()));
            return "redirect:/home";
        }
        model.addAttribute("chirperuser", new ChirperUser());
        if (loginValidation(chirperlogin).equals("nf")) {
            model.addAttribute("noUser", true);
        }
        if (loginValidation(chirperlogin).equals("pw")) {
            model.addAttribute("wrongPw", true);
        }
        return "loginL";
    }

    @PostMapping("signup")
    public String signup(@Valid @ModelAttribute("chirperuser") ChirperUser chirperuser, BindingResult result,
            final Model model) {
        if (signupValidation(chirperuser).equals("ok")) {
            um.save(chirperuser);
            us.setLogged(true);
            cu.setCurrent(chirperuser);
            return "redirect:/home";
        }
        model.addAttribute("chirperlogin", new ChirperLogin());
        if (signupValidation(chirperuser).equals("nick")) {
            model.addAttribute("nick", true);
        }
        if (signupValidation(chirperuser).equals("email")) {
            model.addAttribute("email", true);
        }
        return "loginS";
    }

    public String signupValidation(ChirperUser cu) {
        ChirperUser find = um.findByEmail(cu.getEmail());
        if (find != null)
            return "email";
        find = um.findByNick(cu.getNick());
        if (find != null)
            return "nick";
        return "ok";
    }

    public String loginValidation(ChirperLogin cl) {
        ChirperUser find = um.findByNick(cl.getNickname());
        if (find == null)
            return "nf";
        if (!find.getPassword().equals(cl.getPassword()))
            return "pw";
        return "ok";
    }
}
