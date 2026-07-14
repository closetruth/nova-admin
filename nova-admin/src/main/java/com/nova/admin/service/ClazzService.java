package com.nova.admin.service;

import com.nova.admin.pojo.Clazz;
import com.nova.admin.pojo.ClazzQueryParam;
import com.nova.admin.pojo.PageResult;

import java.util.List;

public interface ClazzService {

    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);

    void delete(Integer id);

    void save(Clazz clazz);

    Clazz get(Integer id);

    void update(Clazz clazz);

    List<Clazz> listAllClazzs();
}
