package com.eseict.reapi.hams.equip.repository;

import com.eseict.reapi.hams.equip.domain.TbEquipmentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TbEquipmentDataRepository extends JpaRepository<TbEquipmentData, Long> {

    @Query("SELECT e FROM TbEquipmentData e WHERE e.usable = 'Y'")
    List<TbEquipmentData> findAllUsable();

    @Query("SELECT e FROM TbEquipmentData e WHERE e.id = :id AND e.usable = 'Y'")
    Optional<TbEquipmentData> findByIdUsable(@Param("id") Long id);
}
