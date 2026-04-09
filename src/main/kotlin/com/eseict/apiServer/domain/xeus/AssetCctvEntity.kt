package com.eseict.apiServer.domain.xeus

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "asset_cctv", schema = ApiConstants.DB_SCHEMA.XEUS)
class AssetCctvEntity {
    @Id
    @Column(name = "mgr_no", nullable = false)
    var mgrNo: String? = null

    @Basic
    @Column(name = "org_mgr_no", nullable = true)
    var orgMgrNo: String? = null

    @Basic
    @Column(name = "md_mgr_no", nullable = true)
    var mdMgrNo: String? = null

    @Basic
    @Column(name = "vms_mgr_no", nullable = true)
    var vmsMgrNo: String? = null

    @Basic
    @Column(name = "site_mgr_no", nullable = true)
    var siteMgrNo: String? = null

    @Basic
    @Column(name = "cctv_nm", nullable = true)
    var cctvNm: String? = null

    @Basic
    @Column(name = "inst_dat", nullable = true)
    var instDat: String? = null

    @Basic
    @Column(name = "device_id", nullable = true)
    var deviceId: String? = null

    @Basic
    @Column(name = "chnl_no", nullable = true)
    var chnlNo: java.math.BigInteger? = null

    @Basic
    @Column(name = "gbn_cd", nullable = false)
    var gbnCd: String? = null

    @Basic
    @Column(name = "ip_addr", nullable = true)
    var ipAddr: String? = null

    @Basic
    @Column(name = "port_num", nullable = true)
    var portNum: Int? = null

    @Basic
    @Column(name = "use_yn", nullable = false)
    var useYn: String? = null

    @Basic
    @Column(name = "light_yn", nullable = false)
    var lightYn: String? = null

    @Basic
    @Column(name = "infrd_yn", nullable = false)
    var infrdYn: String? = null

    @Basic
    @Column(name = "pan_yn", nullable = false)
    var panYn: String? = null

    @Basic
    @Column(name = "tilt_yn", nullable = false)
    var tiltYn: String? = null

    @Basic
    @Column(name = "zoom_yn", nullable = false)
    var zoomYn: String? = null

    @Basic
    @Column(name = "talk_yn", nullable = false)
    var talkYn: String? = null

    @Basic
    @Column(name = "tour_yn", nullable = false)
    var tourYn: String? = null

    @Basic
    @Column(name = "con_id", nullable = true)
    var conId: String? = null

    @Basic
    @Column(name = "con_pwd", nullable = true)
    var conPwd: String? = null

    @Basic
    @Column(name = "snmp_str", nullable = true)
    var snmpStr: String? = null

    @Basic
    @Column(name = "const_year", nullable = true)
    var constYear: String? = null

    @Basic
    @Column(name = "const_nm", nullable = true)
    var constNm: String? = null

    @Basic
    @Column(name = "loc_desc", nullable = true)
    var locDesc: String? = null

    @Basic
    @Column(name = "rmark", nullable = true)
    var rmark: String? = null

    @Basic
    @Column(name = "_gid", nullable = false)
    var gid: Int? = null

    @Basic
    @Column(name = "_annox", nullable = true)
    var annox: java.math.BigDecimal? = null

    @Basic
    @Column(name = "_annoy", nullable = true)
    var annoy: java.math.BigDecimal? = null

    @Basic
    @Column(name = "_geometry", nullable = true)
    var geometry: String? = null

    @Basic
    @Column(name = "cctv_desc", nullable = true)
    var cctvDesc: String? = null

    @Basic
    @Column(name = "angle", nullable = true)
    var angle: Int? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "mgrNo = $mgrNo " +
                "orgMgrNo = $orgMgrNo " +
                "mdMgrNo = $mdMgrNo " +
                "vmsMgrNo = $vmsMgrNo " +
                "siteMgrNo = $siteMgrNo " +
                "cctvNm = $cctvNm " +
                "instDat = $instDat " +
                "deviceId = $deviceId " +
                "chnlNo = $chnlNo " +
                "gbnCd = $gbnCd " +
                "ipAddr = $ipAddr " +
                "portNum = $portNum " +
                "useYn = $useYn " +
                "lightYn = $lightYn " +
                "infrdYn = $infrdYn " +
                "panYn = $panYn " +
                "tiltYn = $tiltYn " +
                "zoomYn = $zoomYn " +
                "talkYn = $talkYn " +
                "tourYn = $tourYn " +
                "conId = $conId " +
                "conPwd = $conPwd " +
                "snmpStr = $snmpStr " +
                "constYear = $constYear " +
                "constNm = $constNm " +
                "locDesc = $locDesc " +
                "rmark = $rmark " +
                "gid = $gid " +
                "annox = $annox " +
                "annoy = $annoy " +
                "geometry = $geometry " +
                "cctvDesc = $cctvDesc " +
                "angle = $angle " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AssetCctvEntity

        if (mgrNo != other.mgrNo) return false
        if (orgMgrNo != other.orgMgrNo) return false
        if (mdMgrNo != other.mdMgrNo) return false
        if (vmsMgrNo != other.vmsMgrNo) return false
        if (siteMgrNo != other.siteMgrNo) return false
        if (cctvNm != other.cctvNm) return false
        if (instDat != other.instDat) return false
        if (deviceId != other.deviceId) return false
        if (chnlNo != other.chnlNo) return false
        if (gbnCd != other.gbnCd) return false
        if (ipAddr != other.ipAddr) return false
        if (portNum != other.portNum) return false
        if (useYn != other.useYn) return false
        if (lightYn != other.lightYn) return false
        if (infrdYn != other.infrdYn) return false
        if (panYn != other.panYn) return false
        if (tiltYn != other.tiltYn) return false
        if (zoomYn != other.zoomYn) return false
        if (talkYn != other.talkYn) return false
        if (tourYn != other.tourYn) return false
        if (conId != other.conId) return false
        if (conPwd != other.conPwd) return false
        if (snmpStr != other.snmpStr) return false
        if (constYear != other.constYear) return false
        if (constNm != other.constNm) return false
        if (locDesc != other.locDesc) return false
        if (rmark != other.rmark) return false
        if (gid != other.gid) return false
        if (annox != other.annox) return false
        if (annoy != other.annoy) return false
        if (geometry != other.geometry) return false
        if (cctvDesc != other.cctvDesc) return false
        if (angle != other.angle) return false

        return true
    }

}

