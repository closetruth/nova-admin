package com.nova.admin.controller;

import com.nova.admin.pojo.OperateLog;
import com.nova.admin.pojo.OperateLogQueryParam;
import com.nova.admin.pojo.PageResult;
import com.nova.admin.pojo.Result;
import com.nova.admin.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/log")
@RestController
@Slf4j
public class LogController {
    @Autowired private OperateLogService operateLogService;

    @GetMapping("/page")
    public Result page(OperateLogQueryParam operateLogQueryParam) {
        log.info("分页查询操作日志");
        PageResult<OperateLog> pageResult = operateLogService.page(operateLogQueryParam);
        return Result.success(pageResult);
    }
}
