package com.eseict.apiServer.repository.rino.fms

import com.eseict.apiServer.domain.rino.fms.ErfFacClfyAddInfo
import com.eseict.apiServer.domain.rino.fms.ErfFacClfyAddInfoPk
import org.springframework.data.jpa.repository.JpaRepository

interface FacClfyAddInfoRepo : JpaRepository<ErfFacClfyAddInfo, String> {


}