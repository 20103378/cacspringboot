package com.base.entity.vo;

import lombok.Data;

@Data
public class RequestUserVO {
    private String email;//   邮箱也是登录帐号
    private String pwd;//   登录密码
}
