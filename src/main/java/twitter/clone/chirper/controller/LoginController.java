package twitter.clone.chirper.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import twitter.clone.chirper.domain.ChirperLogin;
import twitter.clone.chirper.domain.ChirperUser;

@Controller
public class LoginController {
    @GetMapping("/")
    public String start(@ModelAttribute("chirperuser") ChirperUser chirperuser,
            @ModelAttribute("chirperlogin") ChirperLogin chirperlogin) {
        return "loginS";
    }

    @PostMapping("login")
    public String login(@Valid @ModelAttribute("chirperlogin") ChirperLogin chirperlogin, BindingResult result,
            final Model model) {
        if (result.hasErrors()) {
            model.addAttribute("chirperuser", new ChirperUser());
            return "loginL";
        }
        return "redirect:/home";
    }

    @PostMapping("signup")
    public String signup(@Valid @ModelAttribute("chirperuser") ChirperUser chirperuser, BindingResult result,
            final Model model) {
        if (result.hasErrors()) {
            model.addAttribute("chirperlogin", new ChirperLogin());
            return "loginS";
        }
        return "redirect:/home";
    }
}