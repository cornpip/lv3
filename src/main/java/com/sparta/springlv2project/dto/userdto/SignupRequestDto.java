package com.sparta.springlv2project.dto.userdto;

import lombok.Getter;
@Getter
public class SignupRequestDto {
        private String username;
        private String password;

        //나중에 Admin 여부 추가해주려고 일부러 분리해놓음.
}
