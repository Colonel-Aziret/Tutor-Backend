package com.example.tutor.service;

import com.example.tutor.db.enums.HttpMethodType;
import com.example.tutor.db.enums.ProcessObjectType;
import com.example.tutor.model.actionLog.filter.ActionLogFilter;
import com.example.tutor.model.actionLog.response.PageActionLogResponse;
import jakarta.servlet.http.HttpServletRequest;


public interface ActionLogService {
    void create(
            HttpServletRequest request,
            ProcessObjectType objectType,
            HttpMethodType methodType,
            String details
    );

    PageActionLogResponse getAll(ActionLogFilter filter);
}
