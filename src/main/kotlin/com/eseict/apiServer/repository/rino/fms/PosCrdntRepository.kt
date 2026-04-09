package com.eseict.apiServer.repository.rino.fms

import com.eseict.apiServer.domain.rino.fms.ErfPosCrdnt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PosCrdntRepository : JpaRepository<ErfPosCrdnt, String> {


    @Query(
        """
        SELECT P
        FROM ErfPosCrdnt P
        WHERE P.facId =:facId
                AND P.useYn = 1
    """
    )
    fun findByFacId(facId: String): List<ErfPosCrdnt>?

}