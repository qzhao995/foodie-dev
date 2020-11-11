package com.qzhao.controller;

import com.qzhao.pojo.Users;
import com.qzhao.pojo.bo.UserBo;
import com.qzhao.service.UsersService;
import com.qzhao.utils.CookieUtils;
import com.qzhao.utils.IMOOCJSONResult;
import com.qzhao.utils.JsonUtils;
import com.qzhao.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
@Api(value = "注册登录",tags = {"用户注册登录相关接口"})
public class PassportController {
    @Resource
    UsersService usersService;

    @GetMapping("/usernameIsExist")
    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    public IMOOCJSONResult exist(@RequestParam String username){
        if(StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名称不能为空");
        }

        boolean b = usersService.queryUsernameIsExist(username);
        if (b){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        // 3. 请求成功，用户名没有重复
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册",notes ="用户注册" ,httpMethod = "POST")
    @PostMapping(value = "save")
    public IMOOCJSONResult save(@RequestBody UserBo userBo,
                                HttpServletRequest request, HttpServletResponse response){
        //1 判断输入参数
        String username =  userBo.getUsername();
        String password = userBo.getPassword();
        String configPassword = userBo.getConfirmPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(configPassword)){
            return IMOOCJSONResult.errorMsg("用户名与密码不能为空");
        }
        //2 判断用户名是否注册过
        boolean b = usersService.queryUsernameIsExist(userBo.getUsername());
        if (b){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }

        //
        if (password.length()<6){
            return IMOOCJSONResult.errorMsg("密码长度不小于6位");
        }
        if (!password.equals(configPassword)){
            return IMOOCJSONResult.errorMsg("两次输入的密码不一致");
        }


        Users users = usersService.SaveUsers(userBo);
        Users result = setNullProperty(users);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(result),true);

        return IMOOCJSONResult.ok(users);
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBo userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 实现登录
        Users userResult = usersService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));

        if (userResult == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);


        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据

        return IMOOCJSONResult.ok(userResult);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据

        return IMOOCJSONResult.ok();
    }


    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }



}
