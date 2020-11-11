package com.qzhao.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户对象BO", description = "从客户端，由用户传入的数据封装在此entity中")
@Data
public class UserBo {

    @ApiModelProperty(value = "用户名", name = "username", example = "imooc", required = true)
    String username;
    @ApiModelProperty(value = "密码",name = "password",example ="123321",required = true)
    String password;
    @ApiModelProperty(value = "确认密码",name = "confirmPassword",example = "123321",required = false)
    String confirmPassword;
}
