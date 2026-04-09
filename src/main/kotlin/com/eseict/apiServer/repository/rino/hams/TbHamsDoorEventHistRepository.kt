package com.eseict.apiServer.repository.rino.hams

import com.eseict.apiServer.domain.rino.hams.TbHamsDoorEventHist
import com.eseict.apiServer.domain.rino.hams.TbHamsDoorHist
import org.springframework.data.jpa.repository.JpaRepository

interface TbHamsDoorEventHistRepository : JpaRepository<TbHamsDoorEventHist, String> {}