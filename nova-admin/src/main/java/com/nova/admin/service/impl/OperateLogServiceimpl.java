package com.nova.admin.service.impl;

import com.nova.admin.mapper.OperateLogMapper;
import com.nova.admin.pojo.OperateLog;
import com.nova.admin.pojo.OperateLogQueryParam;
import com.nova.admin.pojo.PageResult;
import com.nova.admin.service.OperateLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OperateLogServiceimpl implements OperateLogService {
    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult<OperateLog> page(OperateLogQueryParam operateLogQueryParam) {
        log.info("分页查询操作日志");
        Integer page = operateLogQueryParam.getPage() != null ? operateLogQueryParam.getPage() : 1;
        Integer pageSize = operateLogQueryParam.getPageSize() != null ? operateLogQueryParam.getPageSize() : 10;
        PageHelper.startPage(page, pageSize);
        List<OperateLog> operateLogList = operateLogMapper.list(operateLogQueryParam);
        Page<OperateLog> p = (Page<OperateLog>) operateLogList;
        return new PageResult<OperateLog>(p.getTotal(), p.getResult());
    }
}
