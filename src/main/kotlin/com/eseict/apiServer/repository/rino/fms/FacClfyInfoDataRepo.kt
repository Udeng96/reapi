package com.eseict.apiServer.repository.rino.fms

import com.eseict.apiServer.domain.rino.fms.ErfFacClfyAddInfoData
import com.eseict.apiServer.domain.rino.fms.ErfFacClfyAddInfoDataPk
import org.springframework.data.jpa.repository.JpaRepository

interface FacClfyInfoDataRepo : JpaRepository<ErfFacClfyAddInfoData, ErfFacClfyAddInfoDataPk> {
}