package com.eseict.apiServer.repository.rino.event

import com.eseict.apiServer.domain.rino.event.IocStatEvetOutbHist
import org.springframework.data.jpa.repository.JpaRepository

interface EvetOutbRepository : JpaRepository<IocStatEvetOutbHist, String> {


    fun findByStatEvetOutbSeqn(statEvetOutbSeqn: String): IocStatEvetOutbHist

}