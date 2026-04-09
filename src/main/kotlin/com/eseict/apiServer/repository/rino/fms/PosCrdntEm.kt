package com.eseict.apiServer.repository.rino.fms

import com.eseict.apiServer.domain.rino.fms.ErfPosCrdnt
import com.eseict.apiServer.repository.rino.RinoRepositorySupport
import org.springframework.stereotype.Repository
import javax.persistence.EntityTransaction


@Repository
class PosCrdntEm : RinoRepositorySupport<ErfPosCrdnt>() {

    fun updatePos(facId: String, lat: Double, lng: Double): Boolean {

        var result = false

        var em = getEntityManager()
        val tr: EntityTransaction = em.transaction
        tr.begin()
        try {
            try {

                var q = StringBuffer()
                q.append(" UPDATE ErfPosCrdnt P SET P.XCrdnt = :lng , P.YCrdnt = :lat ")
                q.append("  WHERE P.facId = :facId ")
                q.append("  AND P.useYn = true ")


                var query = em.createQuery(q.toString())

                query.setParameter("lng", lng.toString())
                query.setParameter("lat", lat.toString())
                query.setParameter("facId", facId.toString())

                var updateCnt = query.executeUpdate()
                tr.commit()

                if (updateCnt > 0) {
                    result = true
                }

            } catch (e: Exception) {
                LOGGER.error(e.message, e)
                tr.rollback()
            }

        } catch (e: Exception) {
            LOGGER.error(e.message, e)
        } finally {
            if (em.isOpen) em.close()
        }

        return result
    }

    fun delete(facId: String): Boolean {
        var result = false

        var em = getEntityManager()
        val tr: EntityTransaction = em.transaction
        tr.begin()
        try {
            try {

                var q = StringBuffer()
                q.append("  UPDATE ErfPosCrdnt POS ")
                q.append("  set POS.useYn = false ")
                q.append("  WHERE POS.facId = :facId   ")

                var query = em.createQuery(q.toString())
                query.setParameter("facId", facId)
                var updateCnt = query.executeUpdate()

                tr.commit()
                if (updateCnt > 0) {
                    result = true
                }

            } catch (e: Exception) {
                LOGGER.error(e.message, e)
                tr.rollback()
            }

        } catch (e: Exception) {
            LOGGER.error(e.message, e)
        } finally {
            if (em.isOpen) em.close()
        }

        return result
    }


}