package oit.is.rumba.field_arena.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RuleController {
  @GetMapping("/rule")        //"/rule"というゲットリクエストがあるとrule.htmlを表示
  public String rule() {
    return "rule.html";
  }
}
