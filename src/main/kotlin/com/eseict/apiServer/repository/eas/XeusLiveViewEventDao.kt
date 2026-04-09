package com.eseict.apiServer.repository.eas

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.domain.eas.LiveViewMobileInfo
import com.eseict.apiServer.domain.eas.XeusEventObj
import com.eseict.common.log.LoggerManager
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.NoResultException


@Repository("xeusLiveViewEventDao")
class XeusLiveViewEventDao(
    @Qualifier(value = "rinoEm")
    val emf: EntityManagerFactory? = null
) {

    private final val _logger = LoggerFactory.getLogger(XeusLiveViewEventDao::class.java)


    private fun getEntityManager(): EntityManager {
        return emf!!.createEntityManager()
    }

    fun selectLiveViewMobileList(): List<LiveViewMobileInfo?>? {
        var result: List<LiveViewMobileInfo?>? = null
        val em = getEntityManager()
        try {
            val queryStr = "SELECT l FROM LiveViewMobileInfo l WHERE l.mobileCheck = :mobileChk"
            val query = em.createQuery(queryStr)
            query.setParameter("mobileChk", "1")
            result = query.resultList as List<LiveViewMobileInfo?>?
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (em.isOpen) {
                em.close()
            }
        }
        return result
    }

    fun selectLiveViewMobileInfo(mgrNo: String?): LiveViewMobileInfo? {
        var info: LiveViewMobileInfo? = null
        val em = getEntityManager()
        try {
            val queryStr = "SELECT l FROM LiveViewMobileInfo l WHERE l.mgrNo = :mgrNo"
            val query = em.createQuery(queryStr)
            query.setParameter("mgrNo", mgrNo)
            info = query.singleResult as LiveViewMobileInfo
        } catch (e: NoResultException) {
            _logger.info("Find Mobile Info by Phone Number -> no Exist")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (em.isOpen) {
                em.close()
            }
        }
        return info
    }

    fun insertLiveViewMobileInfo(info: LiveViewMobileInfo?): String {
        var result: String = ApiConstants.ResponseConstants.RESPONSE_FAIL
        val em = getEntityManager()
        try {
            em.transaction.begin()
            em.persist(info)
            em.transaction.commit()
            result = ApiConstants.ResponseConstants.RESPONSE_SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (em.isOpen) {
                em.close()
            }
        }
        return result
    }

    fun updateLiveViewMobileInfo(info: LiveViewMobileInfo): String {
        var result: String = ApiConstants.ResponseConstants.RESPONSE_FAIL
        val em = getEntityManager()
        try {
            em.transaction.begin()
            val find: LiveViewMobileInfo = em.find(LiveViewMobileInfo::class.java, info.mgrNo)
            find.mobileCheck = info.mobileCheck
            find.mbUrl = info.mbUrl
            find.liveTitle = info.liveTitle
            find.lat = info.lat
            find.lng = info.lng
            em.merge<Any>(find)
            em.transaction.commit()
            result = ApiConstants.ResponseConstants.RESPONSE_SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (em.isOpen) {
                em.close()
            }
        }
        return result
    }

    fun selectXeusLiveViewEvnetList(): List<XeusEventObj?>? {
        var list: List<XeusEventObj?> = ArrayList<XeusEventObj?>()
        val em = getEntityManager()
        try {
            val queryStr = "SELECT h FROM XeusEventObj h WHERE h.procSt is not :procSt"
            val query = em.createQuery(queryStr)
            query.setParameter("procSt", "91")
            list = query.resultList as List<XeusEventObj?>
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (em.isOpen) {
                em.close()
            }
        }
        return list
    }

    fun selectLiveViewEventNow(phoneNumber: String?): String {
        val result: String = ApiConstants.ResponseConstants.RESPONSE_FAIL
        val em = getEntityManager()
        try {
            val queryStr = "SELECT h FROM XeusEventObj h WHERE h.procSt = :procSt AND h.phoneNumber = :phoneNumber"
            val query = em.createQuery(queryStr)
            query.setParameter("procSt", "10")
            query.setParameter("phoneNumber", phoneNumber)
            query.singleResult
        } catch (e: NoResultException) {
            return ApiConstants.ResponseConstants.RESPONSE_SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (em.isOpen) {
                em.close()
            }
        }
        return result
    }

    fun insertNewLiveViewEvent(eventObj: XeusEventObj?): String {
        var result: String = ApiConstants.ResponseConstants.RESPONSE_FAIL
        val em = getEntityManager()
        try {
            em.transaction.begin()
            em.persist(eventObj)
            em.transaction.commit()
            result = ApiConstants.ResponseConstants.RESPONSE_SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (em.isOpen) {
                em.close()
            }
        }
        return result
    }

    fun updateLiveViewEvent(eventObj: XeusEventObj): String {
        var result: String = ApiConstants.ResponseConstants.RESPONSE_FAIL
        val em = getEntityManager()
        try {
            val queryStr =
                "SELECT h FROM XeusEventObj h WHERE h.phoneNumber = :phoneNumber AND h.statEvetOutbDtm = (SELECT MAX(se.statEvetOutbDtm) FROM XeusEventObj se WHERE se.phoneNumber = :phoneNumber)"
            val query = em.createQuery(queryStr)
            query.setParameter("phoneNumber", eventObj.phoneNumber)
            val find: XeusEventObj = query.singleResult as XeusEventObj
            find.procSt = eventObj.procSt
            find.locX = eventObj.locX
            find.locY = eventObj.locY
            em.transaction.begin()
            em.merge<Any>(find)
            em.transaction.commit()
            result = ApiConstants.ResponseConstants.RESPONSE_SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (em.isOpen) {
                em.close()
            }
        }
        return result
    }
}