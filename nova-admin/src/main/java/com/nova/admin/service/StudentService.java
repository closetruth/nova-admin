package com.nova.admin.service;

import com.nova.admin.pojo.PageResult;
import com.nova.admin.pojo.Student;
import com.nova.admin.pojo.StudentQueryParam;

import java.util.List;

public interface StudentService {
    PageResult<Student> page(StudentQueryParam studentQueryParam);

    void save(Student student);

    Student get(Integer id);

    void update(Student student);

    void delete(List<Integer> ids);

    void updateViolation(Integer id, Short score);
}
