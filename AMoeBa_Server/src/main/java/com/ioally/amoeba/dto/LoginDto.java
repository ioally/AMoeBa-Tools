package com.ioally.amoeba.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 登陆数据承载dto
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginDto extends BaseRequestDto implements Serializable {

    private String userName;

    private String passWord;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public LoginDto() {
    }

    public LoginDto(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (obj instanceof LoginDto) {
                LoginDto loginDto = (LoginDto) obj;
                return StringUtils.isNotEmpty(this.userName)
                        && StringUtils.isNotEmpty(this.passWord)
                        && this.userName.equals(loginDto.userName)
                        && this.passWord.equals(loginDto.passWord);
            } else {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        return "LoginDto{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }
}
