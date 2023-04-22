package com.example.usercenter.contoller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenter.common.BaseResponse;
import com.example.usercenter.common.ErrorCode;
import com.example.usercenter.common.ResultUtils;
import com.example.usercenter.exception.BusinessException;
import com.example.usercenter.model.domain.User;
import com.example.usercenter.model.domain.request.UserLoginRequest;
import com.example.usercenter.model.domain.request.UserRegisterRequest;
import com.example.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.example.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://81.71.152.67:8080"}, allowCredentials = "true")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        //return new BaseResponse<>(0,result,ok);
        return  ResultUtils.success(result);
    }

    @PostMapping("/login")
    public  BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginrRequest, HttpServletRequest request) {
        if (userLoginrRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginrRequest.getUserAccount();
        String userPassword = userLoginrRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount,userPassword,request);
//        return new BaseResponse<>(0,user,"ok");
        return  ResultUtils.success(user);
    }
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
//        return userService.userLogout(request);
        return  ResultUtils.success(result);
    }





    @GetMapping("/search")
    public BaseResponse <List<User>> searchUsers(String username, HttpServletRequest request){
        // 仅管理员可查询
//        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
//        User user = (User) userObj;
//        if (user == null || user.getUserRole()!=ADMIN_ROLE) {
//            return new ArrayList<>();
//        }
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());//遍历 数据流
        return ResultUtils.success(list);
    }
//            user.setUserPassword(null);
//            return user;

//            用户脱敏
//






    @PostMapping("/delete")
    public BaseResponse <Boolean> deleteUsers(@RequestBody long id,HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        // 仅管理员可删除
//        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
//        User user = (User) userObj;
//        if (user == null || user.getUserRole()!=ADMIN_ROLE) {
//            return false;
//        }

        if (id<=0){
            return null;
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
//        return userService.removeById(id);

    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){

        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user !=null && user.getUserRole() == ADMIN_ROLE;
        }



    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        //获取用户登录态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;

        //如果当前用户为空，直接返回null
        if (currentUser == null) {
            return null;
        }
        //获取当前用户ID
        long userId = currentUser.getId();
        //TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);

        //脱敏
//        return userService.getSafetyUser(user);
        return  ResultUtils.success(safetyUser);
    }
    }

