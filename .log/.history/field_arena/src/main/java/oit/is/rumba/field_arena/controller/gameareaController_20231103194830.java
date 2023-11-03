package oit.is.rumba.field_arena.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gamearea")
public class gameareaController {
    @GetMapping("step9")
    public String sample39(Principal prin, ModelMap model) {
        String loginUser = prin.getName();
        model.addAttribute("loginUser", loginUser);

        return "sample37.html";
    }
}
