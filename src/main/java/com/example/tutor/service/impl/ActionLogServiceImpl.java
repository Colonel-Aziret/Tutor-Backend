package com.example.tutor.service.impl;

import com.example.tutor.controller.BaseController;
import com.example.tutor.db.entity.ActionLog;
import com.example.tutor.db.enums.HttpMethodType;
import com.example.tutor.db.enums.ProcessObjectType;
import com.example.tutor.db.repository.ActionLogRepository;
import com.example.tutor.db.repository.specification.ActionLogSpecification;
import com.example.tutor.model.actionLog.filter.ActionLogFilter;
import com.example.tutor.model.actionLog.response.PageActionLogResponse;
import com.example.tutor.service.ActionLogService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActionLogServiceImpl implements ActionLogService {

    private final ActionLogRepository repository;
    private final BaseController baseController;

    @Override
    public void create(
            HttpServletRequest request,
            ProcessObjectType objectType,
            HttpMethodType methodType,
            String details
    ) {
        ActionLog log = ActionLog.builder()
                .user(baseController.getUserFromContext())
                .objectType(objectType)
                .httpMethodType(methodType)
                .details(details)
                .remoteIp(baseController.tryDetectRemoteClientIp(request))
                .build();

        repository.save(log);
    }

    @Override
    public PageActionLogResponse getAll(ActionLogFilter filter) {
        Pageable pageable = PageRequest.of(BaseController.getPage(filter.getPage()), filter.getSize(),
                Sort.by(Sort.Direction.DESC,
                        "createdTime"));
        ActionLogSpecification specification = new ActionLogSpecification(filter);

        Page<ActionLog> page = repository.findAll(specification, pageable);

        return PageActionLogResponse.from(page);
    }

}
