package com.example.tutor.service.impl;

import com.example.tutor.controller.BaseController;
import com.example.tutor.db.entity.sys.SysLogRequest;
import com.example.tutor.db.repository.SysLogRequestRepository;
import com.example.tutor.service.SysLogRequestService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SysLogsRequestServiceImpl implements SysLogRequestService {

    private final SysLogRequestRepository repository;
    private final BaseController baseController;

    @Override
    public void save(SysLogRequest request) {

    }

    @Override
    public void saveSuccessToDb(
            String className,
            String methodName,
            String message,
            HttpServletRequest httpServletRequest
    ) {
        var log = new SysLogRequest(
                className,
                methodName,
                message,
                BaseController.Constants.LOG_STATUS.SUCCESS.name(),
                baseController.getUserFromContext(),
                baseController.tryDetectRemoteClientIp(httpServletRequest)
        );
        repository.save(log);
    }

    @Override
    public void saveSuccessToFileAndDb(
            String nameClass,
            String nameFunction,
            String message,
            HttpServletRequest httpServletRequest
    ) {
        Log logger = LogFactory.getLog(nameClass);

        logger.info(nameFunction + " " + message);

        var log = new SysLogRequest(
                nameClass,
                nameFunction,
                message,
                BaseController.Constants.LOG_STATUS.SUCCESS.name(),
                baseController.getUserFromContext(),
                baseController.tryDetectRemoteClientIp(httpServletRequest)
        );
        repository.save(log);
    }

    @Override
    public void saveExceptionToFileAndDb(
            String nameClass,
            Exception e,
            String nameFunction,
            HttpServletRequest httpServletRequest
    ) {
        Log logger = LogFactory.getLog(nameClass);

        logger.error(nameFunction + " exception: " + e.getMessage());

        var log = new SysLogRequest(
                nameClass,
                nameFunction,
                e.getMessage(),
                BaseController.Constants.LOG_STATUS.ERROR.name(),
                baseController.getUserFromContext(),
                baseController.tryDetectRemoteClientIp(httpServletRequest)
        );
        repository.save(log);
    }

    @Override
    public void saveErrorToFileAndDb(
            String nameClass,
            String errorMessage,
            String nameFunction,
            HttpServletRequest httpServletRequest
    ) {
        Log logger = LogFactory.getLog(nameClass);

        logger.error(nameFunction + " exception: " + errorMessage);

        var log = new SysLogRequest(
                nameClass,
                nameFunction,
                errorMessage,
                BaseController.Constants.LOG_STATUS.ERROR.name(),
                baseController.getUserFromContext(),
                baseController.tryDetectRemoteClientIp(httpServletRequest)
        );
        repository.save(log);
    }


}
