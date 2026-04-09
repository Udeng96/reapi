package com.eseict.apiServer.repository.rino.fms

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.domain.rino.fms.ErfFacClfy
import com.eseict.apiServer.domain.rino.fms.ErfFacInfo
import com.eseict.apiServer.domain.rino.fms.ErfPosCrdnt
import com.eseict.apiServer.dto.GisFacInfo
import com.eseict.apiServer.repository.rino.RinoRepositorySupport
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.EntityTransaction
import javax.persistence.PersistenceContext
import javax.persistence.PersistenceException
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class FacEm : RinoRepositorySupport<ErfFacInfo>() {

    final val logger: Logger = LoggerFactory.getLogger(FacEm::class.java)

    @PersistenceContext
    var emf: EntityManager? = null

    override fun update(fac: ErfFacInfo): Boolean {
        var result = false

        var em = getEntityManager()
        val tr: EntityTransaction = em.transaction
        tr.begin()
        try {
            try {

                var q = StringBuffer()
                q.append("  UPDATE ErfFacInfo FAC ")
                q.append("  set FAC.facNm =:facNm, ")
                q.append("      FAC.clientCd = :clientCd, ")
                q.append("      FAC.siteCd = :siteCd, ")
                q.append("      FAC.facClfyId = :facClfyId, ")
                q.append("      FAC.posNm = :posNm, ")
                q.append("      FAC.addrShort = :addrShort, ")
                q.append("      FAC.area = :area, ")
                q.append("      FAC.mgtNo = :mgtNo ")
                q.append("  WHERE FAC.facId = :facId   ")

                var query = em.createQuery(q.toString())
                query.setParameter("facId", fac.facId)
                query.setParameter("facNm", fac.facNm)
                query.setParameter("facClfyId", fac.facClfyId)
                query.setParameter("posNm", fac.posNm)
                query.setParameter("addrShort", fac.posNm)
                query.setParameter("area", fac.area)

                var mgtNo = fac.mgtNo
                try {
                    mgtNo = fac.area.plus(ApiConstants.STR_SPLIT).plus(fac.mgtNo!!.split("_")[1])
                } catch (e: Exception) {
                }

                query.setParameter("mgtNo", mgtNo)

                var updateCnt = query.executeUpdate()

//                var find:ErfFacInfo = em.find(ErfFacInfo::class.java, fac.facId)
//
//                find.facNm = fac.facNm
//                find.clientCd = fac.clientCd
//                find.siteCd = fac.siteCd
//                find.facClfyId = fac.facClfyId
//                find.posNm = fac.posNm
//                find.addrShort = fac.posNm
//                find.area = fac.area
//                find.mgtNo = fac.area.plus(ApiConstants.STR_SPLIT).plus(find.mgtNo!!.split("_")[1])

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
                q.append("  UPDATE ErfFacInfo FAC ")
                q.append("  set FAC.useYn = 0 ")
                q.append("  WHERE FAC.facId = :facId   ")

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


    fun saveAll(list: List<ErfFacInfo>): Boolean {
        val em = getEntityManager()
        val transaction = em.transaction
        var result = false
        transaction.begin()
        result = try {
            for (fac in list) {
                em.persist(fac)
            }
            transaction.commit()
            true
        } catch (e: IllegalStateException) {
            transaction.rollback()
            LOGGER.error(e.message, e)
            throw e
        } catch (e: IllegalArgumentException) {
            transaction.rollback()
            LOGGER.error(e.message, e)
            throw e
        } catch (e: PersistenceException) {
            transaction.rollback()
            LOGGER.error(e.message, e)
            throw e
        } finally {
            em.close()
        }
        return result
    }

    fun getCctvMgrNo(facNm: String?): String {
        var mgrNoList: List<String> = ArrayList();
        var mgrNo: String = "";
        val em = getEntityManager();
        try {
            val q = StringBuffer();
            q.append("select f.mgtNo from ErfFacInfo f where f.facNm = :facNm and (f.facClfyId = '' or f.facClfyId = 'FCC_0e3b16826fed43e88b7e9beeaa2a1622')");

            var query = em.createQuery(q.toString());
            query.setParameter("facNm", facNm);
            mgrNo = query.singleResult.toString();
        } catch (e: Exception) {
            LOGGER.error(e.message, e);
        }

        return mgrNo;

    }


    fun getFacList(type: String?, facNm: String?, page: Int, rows: Int?, facClfyNm: String? = ""): List<GisFacInfo>? {

        var result: List<GisFacInfo>? = null
        val em = getEntityManager()
        try {

            val cb = em.criteriaBuilder
            val cq = cb.createQuery(GisFacInfo::class.java)
            val root = cq.from(ErfFacInfo::class.java)

            val clfyJoin = root.join<ErfFacInfo, ErfFacClfy>("refErfFacClfy", JoinType.INNER)
            val posJoin = root.join<ErfFacInfo, MutableList<ErfPosCrdnt>>("refErfPosCrdnts", JoinType.INNER)

            val predicates = mutableListOf<Predicate>()

            if (type?.isNotEmpty() == true) {
                predicates.add(cb.and(cb.equal(root.get<String>("facClfyId"), type)))
            }

            if (facNm?.isNotEmpty() == true) {
                predicates.add(cb.like(cb.upper(root.get("facNm")), facNm.uppercase()))
            }
            predicates.add(cb.and(cb.equal(root.get<Int>("useYn"), 1)))
            predicates.add(cb.and(cb.equal(clfyJoin.get<Char>("useYn"), 1)))
            predicates.add(cb.and(cb.equal(posJoin.get<Int>("useYn"), 1)))
            predicates.add(
                cb.and(
                    cb.and(
                        cb.isNotNull(posJoin.get<String>("XCrdnt")),
                        cb.notEqual(posJoin.get<String>("XCrdnt"), ""),
                        cb.lessThanOrEqualTo(
                            cb.diff(
                                cb.length(posJoin.get("XCrdnt")),
                                cb.length(
                                    cb.function(
                                        "replace",
                                        String::class.java,
                                        posJoin.get<String>("XCrdnt"),
                                        cb.literal("."),
                                        cb.literal("")
                                    )
                                )
                            ),
                            1
                        )
                    ),
                    cb.and(
                        cb.isNotNull(posJoin.get<String>("YCrdnt")),
                        cb.notEqual(posJoin.get<String>("YCrdnt"), ""),
                        cb.lessThanOrEqualTo(
                            cb.diff(
                                cb.length(posJoin.get("YCrdnt")),
                                cb.length(
                                    cb.function(
                                        "replace",
                                        String::class.java,
                                        posJoin.get<String>("YCrdnt"),
                                        cb.literal("."),
                                        cb.literal("")
                                    )
                                )
                            ),
                            1
                        )
                    )
                )
            )

            if (facClfyNm?.isNotEmpty() == true) {
                predicates.add(cb.like(cb.upper(clfyJoin.get("facClfyNm")), facClfyNm.uppercase()))
            }


            cq.where(*predicates.toTypedArray())

            cq.orderBy(cb.desc(root.get<String>("facNm")))
            cq.select(
                cb.construct(
                    GisFacInfo::class.java,
                    root.get<String>("facId"),
                    root.get<String>("facNm"),
                    root.get<String>("mgtNo"),
                    root.get<String>("addrShort"),
                    posJoin.get<String>("YCrdnt"),
                    posJoin.get<String>("XCrdnt"),
                    root.get<String>("facClfyId"),
                    clfyJoin.get<String>("facClfyNm"),
                    root.get<Int>("useYn"),
                    root.get<String>("updDtm"),
                    root.get<String>("fileGrupId")
                )
            )

            result = if (page!! < 0) {  //-1 을 요청하는 경우 전체 검색
                em.createQuery(cq).resultList
            } else {
                val first = (page?.minus(1))?.times(rows ?: 10) ?: 0
                em.createQuery(cq).setFirstResult(first).setMaxResults(rows ?: 10).resultList
            }

        } catch (e: Exception) {
            logger.error(e.message, e)
        } finally {
            em.close()
        }
        return result
    }
}