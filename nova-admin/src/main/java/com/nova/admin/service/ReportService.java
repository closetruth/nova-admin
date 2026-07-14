package com.nova.admin.service;

import com.nova.admin.pojo.ClazzData;
import com.nova.admin.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    JobOption getEmpJobData();

    List<Map<String, Object>> getEmpGenderData();

    ClazzData getStudentCountData();

    List<Map<String, Object>> getStudentDegreeData();
}
