package com.example.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -543286974259314538L;
    private String userAccount;
    private String userPassword;
}