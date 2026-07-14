package com.nova.admin.mapper;

import com.nova.admin.pojo.Clazz;
import com.nova.admin.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClazzMapper {


    List<Clazz> list(ClazzQueryParam clazzQueryParam);

    void deleteByIds(Integer id);

    void insert(Clazz clazz);

    Clazz getById(Integer id);

    void updateById(Clazz clazz);

    List<Clazz> listAllClazzs();
}
