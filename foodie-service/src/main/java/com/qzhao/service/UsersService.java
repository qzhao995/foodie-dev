package com.qzhao.service;

import com.qzhao.pojo.Users;
import com.qzhao.pojo.bo.UserBo;

public interface UsersService {
    boolean queryUsernameIsExist(String username);

    Users SaveUsers(UserBo userBo);

    /**
     * 检索用户名和密码是否匹配，用于登录
     */
    public Users queryUserForLogin(String username, String password);
}
