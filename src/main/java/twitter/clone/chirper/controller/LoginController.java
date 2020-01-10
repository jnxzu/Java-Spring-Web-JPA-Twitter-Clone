package twitter.clone.chirper.controller;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
        model.addAttribute("chirperuser", new ChirperUser());
        if (result.hasErrors()) {
            return "loginL";
        }
        if (loginValidation(chirperlogin).equals("nf")) {
            model.addAttribute("noUser", true);
        }
        if (loginValidation(chirperlogin).equals("pw")) {
            model.addAttribute("wrongPw", true);
        }
        if (loginValidation(chirperlogin).equals("ok")) {
            us.setLogged(true);
            if (chirperlogin.getNickname().equals("administrator")) {
                us.setAdmin(true);
            }
            cu.setCurrent(um.findByNick(chirperlogin.getNickname()));
            return "redirect:/home";
        }
        return "loginL";
    }

    @PostMapping("signup")
    public String signup(@Valid @ModelAttribute("chirperuser") ChirperUser chirperuser, BindingResult result,
            final Model model) {
        model.addAttribute("chirperlogin", new ChirperLogin());
        if (result.hasErrors()) {
            return "loginS";
        }
        if (signupValidation(chirperuser).equals("nick")) {
            model.addAttribute("nick", true);
        }
        if (signupValidation(chirperuser).equals("email")) {
            model.addAttribute("email", true);
        }
        if (signupValidation(chirperuser).equals("ok")) {
            um.save(chirperuser);
            us.setLogged(true);
            cu.setCurrent(chirperuser);
            confirmationEmail(chirperuser.getEmail());
            return "redirect:/home";
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

    public void confirmationEmail(String email) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        String from = "chirpverify";
        String pass = "chirperpw";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress toAddress = new InternetAddress(email);
            message.addRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject("Welcome to CHIRPER!");
            message.setText("Thanks for joining us!");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
