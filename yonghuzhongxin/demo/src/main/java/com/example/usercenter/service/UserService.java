package com.example.usercenter.service;

import com.example.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 *用户服务
 * @author wangxian
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param request 脱敏后的用户信息
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**用户脱敏
        @param originUser
        @return
     */
    User getSafetyUser(User originUser);

    /**用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}


