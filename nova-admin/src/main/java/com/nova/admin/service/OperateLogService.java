package com.nova.admin.service;

import com.nova.admin.pojo.OperateLog;
import com.nova.admin.pojo.OperateLogQueryParam;
import com.nova.admin.pojo.PageResult;

public interface OperateLogService {
    PageResult<OperateLog> page(OperateLogQueryParam operateLogQueryParam);
}
