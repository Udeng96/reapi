package com.eseict.apiServer.repository.hams

import com.eseict.apiServer.domain.hams.TbGroupData
import org.springframework.data.jpa.repository.JpaRepository

interface TbGroupDataRepository : JpaRepository<TbGroupData, String> {
}