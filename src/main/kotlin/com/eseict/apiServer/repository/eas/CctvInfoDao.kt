package com.eseict.apiServer.repository.eas

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.dto.mobile.CctvInfoResponse
import com.eseict.apiServer.dto.mobile.XeusAssetCctv
import com.eseict.common.config.ConfFileInfo
import com.eseict.common.datetime.DateTimeUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*
import java.util.stream.Collectors
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

@Repository("cctvInfoDao")
class CctvInfoDao(
    @Qualifier(value = "rinoEm")
    val emf: EntityManagerFactory? = null
) {
    private final val _logger = LoggerFactory.getLogger(CctvInfoDao::class.java)
    private fun getEntityManager(): EntityManager {
        return emf!!.createEntityManager()
    }

    fun insertLiveCctvInfo(assetCctv: XeusAssetCctv): String? {
        var result = ApiConstants.ResponseConstants.RESPONSE_FAIL
        val em = getEntityManager()
        try {
            em.transaction.begin()
            val queryStr = "INSERT INTO xeus.asset_cctv " +
                    "(mgr_no, org_mgr_no, md_mgr_no, vms_mgr_no, site_mgr_no, cctv_nm, inst_dat, device_id, " +
                    "chnl_no, gbn_cd, ip_addr, port_num, use_yn, light_yn, infrd_yn, pan_yn, tilt_yn, zoom_yn, talk_yn, tour_yn, con_id, " +
                    "con_pwd, snmp_str, const_year, const_nm, loc_desc, rmark, " +
                    "_gid, _annox, _annoy," +
                    "_geometry," +
                    " cctv_desc, angle" +  //                    " user_id, play_yn" +
                    ") " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, null, 80, 'Y', 'N', 'N', 'N', 'N', 'N', 'N', 'N', null, " +
                    "null, null, ?, '', '화성시', null, " +
                    "1000, ? , ?," +  //_gid 값을 어떻게 넣어야할지 몰라 고정값으로 셋팅
                    //                    "st_transform(ST_SetSRID(public.ST_MakePoint(?, ?), 4326), 5186), " + //geometry 변환 방식을 다르게함
                    "null," +
                    "null, -1" +  //                    " ?, 'N'" +
                    ");"
            val query = em.createNativeQuery(queryStr)
            query.setParameter(1, assetCctv.mgrNo)
            query.setParameter(2, assetCctv.orgMgrNo)
            query.setParameter(3, assetCctv.mdMgrNo)
            query.setParameter(3, assetCctv.mdMgrNo)
            query.setParameter(4, assetCctv.vmsMgrNo)
            query.setParameter(5, assetCctv.siteMgrNo)
            query.setParameter(6, assetCctv.cctvNm)
            val c = Calendar.getInstance()
            query.setParameter(7, DateTimeUtil.toPlaneDateTimeString(c, DateTimeUtil.TYPE_DATE_TIME, true))
            query.setParameter(8, assetCctv.deviceId)
            query.setParameter(9, assetCctv.chnlNo!!.toInt())
            query.setParameter(10, assetCctv.gbnCd)
            query.setParameter(11, c[Calendar.YEAR])
            //            query.setParameter(12, Float.parseFloat(assetCctv.getLng()));
//            query.setParameter(13, Float.parseFloat(assetCctv.getLat()));
            query.setParameter(12, BigDecimal(assetCctv.lng))
            query.setParameter(13, BigDecimal(assetCctv.lat))

//            query.setParameter(14, assetCctv.getUserId());
            query.executeUpdate()
            em.transaction.commit()
            result = ApiConstants.ResponseConstants.RESPONSE_SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    // 좌표 업데이트 하면 될듯
    fun updateLiveCctvInfo(assetCctv: XeusAssetCctv): String? {
        var result = ApiConstants.ResponseConstants.RESPONSE_FAIL
        val em = getEntityManager()
        try {
            em.transaction.begin()
            val queryStr = "UPDATE xeus.asset_cctv SET " +
                    "device_id = ?, " +  //                    "_geometry = st_transform(ST_SetSRID(public.ST_MakePoint(?, ?), 4326), 5186), cctv_desc = null, angle = -1, user_id = 'ahnch', play_yn = 'N' " +
                    " _annox = ? , _annoy = ?  ,cctv_desc = null, angle = -1" +  //                    " user_id = 'kangdiv', play_yn = 'N' " +
                    "WHERE mgr_no = ?;"
            val query = em.createNativeQuery(queryStr)
            query.setParameter(1, assetCctv.deviceId)
            query.setParameter(2, BigDecimal(assetCctv.lng))
            query.setParameter(3, BigDecimal(assetCctv.lat))
            query.setParameter(4, assetCctv.mgrNo)
            query.executeUpdate()
            em.transaction.commit()
            result = ApiConstants.ResponseConstants.RESPONSE_SUCCESS
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun selectCctvInfoList(isList: Boolean, startIdx: Int, endIdx: Int, cctvName: String): List<CctvInfoResponse?>? {
        var list: List<CctvInfoResponse?>? = ArrayList()
        val em = getEntityManager()
        try {
            // select st_x(ST_Transform(_geometry, 4326)), st_y(ST_Transform(_geometry, 4326)) from xeus.asset_cctv
            var queryStr = "" // String mgrNo, String cctvNm, Double xCrdnt, Double yCrdnt, String gbnCd, String gbnCdNm
            queryStr += " SELECT "
            //            queryStr += "  new com.eseict.eas.domain.apiResponse.CctvInfoResponse(  ";
            queryStr += "   cctv.mgr_no as mgrNo, cctv.cctv_nm as cctvNm, st_x(ST_Transform(cctv._geometry, 4326)) as xCrdnt, st_y(ST_Transform(cctv._geometry, 4326)) as yCrdnt, cctv.gbn_cd as gbnCd, commcd.cde_nm as gbnCdNm "
            //            queryStr += "  )  ";
            queryStr += " FROM "
            queryStr += "       xeus.asset_cctv cctv "
            queryStr += "       LEFT OUTER JOIN "
            queryStr += "       xeus.mt_cmm_cde commcd ON (cctv.gbn_cd = commcd.cde_cde) "
            queryStr += "  "
            queryStr += " WHERE commcd.grp_cde = :grpCde "
            queryStr += " AND cctv.cctv_nm like :cctvName "
            queryStr += " AND cctv.gbn_cd in :gbnCdArray "
            val query = em.createNativeQuery(queryStr)
            query.setParameter("grpCde", "C14") // 그룹 코드 고정
            query.setParameter("cctvName", "%$cctvName%")
            if (isList) {
                query.firstResult = startIdx
                query.maxResults = endIdx
            }
            val gbnCdArray: List<String> = ArrayList(
                Arrays.asList(
                    *ConfFileInfo.get("cctv.gbn.array").split(",".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()))
            query.setParameter("gbnCdArray", gbnCdArray) // 카메라 목록 핓터
            val resultMapList: MutableList<Array<Any>>? = query.resultList as MutableList<Array<Any>>?
            list = resultMapList?.stream()?.map { e: Array<Any>? ->
                val cctvInfoResponse = CctvInfoResponse()
                cctvInfoResponse.mgrNo = (e!![0] as String).trim { it <= ' ' }
                cctvInfoResponse.cctvNm = (e[1] as String).trim { it <= ' ' }
                cctvInfoResponse.gbnCd = (e[4] as String).trim { it <= ' ' }
                cctvInfoResponse.gbnCdNm = (e[5] as String).trim { it <= ' ' }
                cctvInfoResponse.xCrdnt = e[2] as Double
                cctvInfoResponse.yCrdnt = e[3] as Double
                cctvInfoResponse
            }?.collect(Collectors.toList()) ?: null
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            if (em.isOpen) {
                em.close()
            }
        }
        return list
    }


    /**
     * 통영에서는 st_x 이 값을 활용하여 변환처리를 했으나 화성에서는 좌표값이 따로 있음
     *
     * @param mgrNoArray
     * @return
     */
    fun selectLiveViewInfoList(mgrNoArray: List<String?>?): List<CctvInfoResponse?>? {
        var list: List<CctvInfoResponse?>? = ArrayList()
        val em = getEntityManager()
        try {
            var queryStr = ""
            queryStr += " SELECT "
            //            queryStr += "   cctv.mgr_no as mgrNo, cctv.cctv_nm as cctvNm, st_x(ST_Transform(cctv._geometry, 4326)) as xCrdnt, st_y(ST_Transform(cctv._geometry, 4326)) as yCrdnt, cctv.device_id as deviceId ";
            queryStr += "   cctv.mgr_no as mgrNo, cctv.cctv_nm as cctvNm, cctv._annox  as xCrdnt, cctv._annoy as yCrdnt, cctv.device_id as deviceId "
            queryStr += " FROM "
            queryStr += "       xeus.asset_cctv cctv "
            //            queryStr += "       LEFT OUTER JOIN ";
//            queryStr += "       xeus.mt_cmm_cde commcd ON (cctv.gbn_cd = commcd.cde_cde) ";
            queryStr += "  "
            queryStr += " WHERE 1=1 "
            if (mgrNoArray != null) {
                if (!mgrNoArray.isEmpty()) {
                    queryStr += " AND cctv.mgr_no in :mgrNoArray "
                }
            }
            val query = em.createNativeQuery(queryStr)
            if (mgrNoArray != null) {
                if (!mgrNoArray.isEmpty()) {
                    query.setParameter("mgrNoArray", mgrNoArray) // 카메라 목록 핓터
                }
            }
            val resultMapList: MutableList<Array<Any>>? = query.resultList as MutableList<Array<Any>>?
            list = resultMapList?.stream()?.map { e: Array<Any>? ->
                val cctvInfoResponse = CctvInfoResponse()
                cctvInfoResponse.mgrNo = (e!![0] as String).trim { it <= ' ' }
                cctvInfoResponse.cctvNm = (e[1] as String).trim { it <= ' ' }
                cctvInfoResponse.rtspUrl = (e[4] as String).trim { it <= ' ' }
                cctvInfoResponse.xCrdnt = (e[2] as BigDecimal).toDouble()
                cctvInfoResponse.yCrdnt = (e[2] as BigDecimal).toDouble()
                cctvInfoResponse
            }?.collect(Collectors.toList()) ?: null
        } catch (e: java.lang.Exception) {
            _logger.error(e.message, e)
        } finally {
            if (em.isOpen) {
                em.close()
            }
        }
        return list
    }
}