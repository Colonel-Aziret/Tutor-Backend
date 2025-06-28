package com.example.tutor.service;

import com.example.tutor.db.entity.sys.SysRole;
import com.example.tutor.model.role.filter.RoleFilter;
import com.example.tutor.model.role.request.SysRoleRequest;
import com.example.tutor.model.role.response.PageSysRoleResponse;
import com.example.tutor.model.role.response.SysRoleResponseDto;
import jakarta.servlet.http.HttpServletRequest;


public interface SysRoleService {

    SysRole findById(Long id);

    SysRoleResponseDto create(SysRoleRequest roleDto, HttpServletRequest request);

    SysRoleResponseDto update(SysRoleRequest roleDto, HttpServletRequest request);

    void delete(Long id, HttpServletRequest request);

    PageSysRoleResponse getAll(RoleFilter filter);
}
