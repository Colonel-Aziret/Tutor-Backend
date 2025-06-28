package com.example.tutor.service.impl;


import com.example.tutor.db.entity.sys.SysLogAuthorization;
import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.repository.SysLogAuthorizationRepository;
import com.example.tutor.service.SysLogAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysLogsAuthorizationServiceImpl implements SysLogAuthorizationService {

    private final SysLogAuthorizationRepository repository;

    @Override
    public void saveSuccessfulAuth(SysUser user, String ip) {
        repository.save(new SysLogAuthorization(user, ip));
    }
}
