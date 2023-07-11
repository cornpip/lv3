package com.sparta.springlv2project.dto.userdto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignupRequestDto {

    private String username;
    private String password;
    private boolean admin = false;
    private String adminToken = "";
}
