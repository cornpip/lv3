package com.sparta.springlv2project.controller;

import com.sparta.springlv2project.dto.userdto.LoginRequestDto;
import com.sparta.springlv2project.dto.userdto.SignupRequestDto;
import com.sparta.springlv2project.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private static final String HostUrl = "http://localhost:8080";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto){
        System.out.println("signupRequestDto.getPassword() = " + signupRequestDto.getPassword());
        System.out.println("signupRequestDto.getUsername() = " + signupRequestDto.getUsername());
        userService.signup(signupRequestDto);
//        String redirectUrl = "/user/login-page";
//        URI location = URI.create(HostUrl+redirectUrl);
        return ResponseEntity.status(HttpStatus.OK).body("회원 가입이 완료되었습니다 !");
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse res){
        userService.login(loginRequestDto, res);
        String redirectUrl = "/board";
        URI location = URI.create(HostUrl+redirectUrl);
         return ResponseEntity.status(HttpStatus.FOUND).location(location).body("로그인 성공 !");
    }


}
