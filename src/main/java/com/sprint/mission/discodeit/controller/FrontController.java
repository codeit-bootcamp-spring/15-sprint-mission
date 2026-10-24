package com.sprint.mission.discodeit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class FrontController {

  @RequestMapping("/findAll")
  public String findAllFront() {
    return "user-list";
  }
}
