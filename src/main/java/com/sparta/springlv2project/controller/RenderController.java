package com.sparta.springlv2project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RenderController {
    @GetMapping("/board")
    public String test(){
        return "board";
    }
    @GetMapping("/user/login-page")
    public String test2(){
        return "login";
    }
}
