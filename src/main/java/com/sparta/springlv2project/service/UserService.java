package com.sparta.springlv2project.service;

import com.sparta.springlv2project.dto.userdto.LoginRequestDto;
import com.sparta.springlv2project.dto.userdto.SignupRequestDto;
import com.sparta.springlv2project.entity.User;
import com.sparta.springlv2project.entity.UserRoleEnum;
import com.sparta.springlv2project.jwt.JwtUtil;
import com.sparta.springlv2project.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());


        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }
        // 이름과 비밀번호 양식
        validationCheck(username, requestDto.getPassword());

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);
    }

    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()->new IllegalArgumentException("회원을 찾을 수 없습니다.")
        );
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");

        }
        issueNewToken(username, res);
    }

    public void issueNewToken(String username, HttpServletResponse res){
        String token = jwtUtil.createToken(username);
        jwtUtil.addJwtToCookie(token, res);
    }
    public void validationCheck(String username, String password){
        String nameVal= "^[a-z0-9]{4,10}$";
        String passVal="[a-zA-Z0-9`~!@#$%^&*()-_=+]*$";
        if(!Pattern.matches(nameVal, username)) throw new IllegalArgumentException("이름이 올바르지 않습니다. 소문자와 숫자로 4~10자를 입력해주세요.");
        if(!Pattern.matches(passVal, password)) throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.영문 대,소문자와 숫자로 8~15자를 입력해주세요.");
    }
}
