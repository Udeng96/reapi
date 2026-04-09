package com.eseict.apiServer.repository.rino.hams

import com.eseict.apiServer.domain.rino.hams.TbHamsSeq
import org.springframework.data.jpa.repository.JpaRepository

interface TbHamsSeqRepository : JpaRepository<TbHamsSeq, String> {

    fun findFirstByOrderBySendDateDesc(): TbHamsSeq?
}