package com.nova.admin.service;

import com.nova.admin.pojo.Emp;
import com.nova.admin.pojo.EmpQueryParam;
import com.nova.admin.pojo.LoginInfo;
import com.nova.admin.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {
//    PageResult<Emp> page(Integer page, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end);

    PageResult<Emp> page(EmpQueryParam empQueryParam);

    void save(Emp emp);

    void delete(List<Integer> ids);

    Emp getInfo(Integer id);

    void update(Emp emp);

    List<Emp> listAllEmployyees();

    LoginInfo login(Emp emp);
}
