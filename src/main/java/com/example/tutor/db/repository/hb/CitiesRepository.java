package com.example.tutor.db.repository.hb;

import com.example.tutor.db.entity.HB.HBCities;
import com.example.tutor.db.repository.AliasRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CitiesRepository extends JpaRepository<HBCities, Long>, JpaSpecificationExecutor<HBCities>, AliasRepository<HBCities> {
}
