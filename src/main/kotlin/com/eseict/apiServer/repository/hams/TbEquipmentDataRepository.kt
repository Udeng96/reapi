package com.eseict.apiServer.repository.hams

import com.eseict.apiServer.domain.hams.TbEquipmentData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TbEquipmentDataRepository : JpaRepository<TbEquipmentData, Long> {


    @Query(
        """
        SELECT A
            FROM TbEquipmentData A
            WHERE A.usable = :usable
    """
    )
    fun findAllUsable(usable:String): List<TbEquipmentData>?


    @Query(
        """
        SELECT A
            FROM TbEquipmentData A
            WHERE A.usable = '1'
                AND A.id = :id
    """
    )
    fun finByIdUsable(id: Long): TbEquipmentData?
}