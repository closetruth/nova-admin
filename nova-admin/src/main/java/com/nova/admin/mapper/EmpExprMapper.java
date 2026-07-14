package com.nova.admin.mapper;


import com.nova.admin.pojo.ExpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExprMapper {
    void insertBatch(List<ExpExpr> exprlist);

    void deleteByEmpIds(List<Integer> empIds);
}
