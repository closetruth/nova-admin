package com.nova.admin.controller;


import com.nova.admin.pojo.Emp;
import com.nova.admin.pojo.LoginInfo;
import com.nova.admin.pojo.Result;
import com.nova.admin.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private EmpService empService;

    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        log.info("登录: {}", emp);
        LoginInfo info = empService.login(emp);
        if (info != null) {
            return Result.success(info);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
}
