package com.eseict.apiServer.repository.rino.event

import com.eseict.apiServer.domain.rino.event.VwIocEvetInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface VwIocEvetInfoRepository : JpaRepository<VwIocEvetInfo, String> {


}