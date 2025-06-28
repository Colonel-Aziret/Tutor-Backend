package com.example.tutor.db.repository;

import com.example.tutor.db.entity.sys.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long>, AliasRepository<SysRole>,
        JpaSpecificationExecutor<SysRole> {

    @Override
    Optional<SysRole> findByAlias(String alias);

    @Query("SELECT r FROM SysRole r WHERE r.alias = 'FARMER' OR r.alias = 'SUPPLIER'")
    List<SysRole> findFarmerAndSupplierRoles();
}
