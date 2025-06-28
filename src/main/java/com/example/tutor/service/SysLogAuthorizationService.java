package com.example.tutor.service;


import com.example.tutor.db.entity.sys.SysUser;

public interface SysLogAuthorizationService {

    void saveSuccessfulAuth(SysUser user, String ip);

}
