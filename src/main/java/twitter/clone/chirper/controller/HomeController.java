package twitter.clone.chirper.controller;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping(value = "home", method = RequestMethod.POST, consumes = { "multipart/form-data" })
    public String newmsg(final Model model, @Valid final Message msg, Errors errors,
            @RequestParam(name = "image", required = false) MultipartFile file) {
        if (!errors.hasErrors()) {
            msg.setAuthors(Arrays.asList(cu.getCurrent()));
            msg.setHasImage(file == null ? false : true);
            mm.save(msg);
            if (file != null) {
                System.out.println(file);
                try {
                    final String imagePath = "file:/";
                    FileOutputStream output = new FileOutputStream(imagePath + msg.getId() + ".png");
                    output.write(file.getBytes());
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

    @PostMapping("home/deletemsg")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteMsg(@RequestParam("msgId") int msgId) {
        mm.deleteById(msgId);
    }
}