package com.eseict.apiServer.domain.rino.fms

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "erf_fac_info", schema = ApiConstants.DB_SCHEMA.FMS)
class ErfFacInfo {
    @Id
    @Column(name = "fac_id", nullable = false, insertable = false, updatable = false)
    var facId: String? = null

    @Basic
    @Column(name = "addr_long", nullable = true)
    var addrLong: String? = null

    @Basic
    @Column(name = "addr_short", nullable = false)
    var addrShort: String? = null

    @Basic
    @Column(name = "aprv_st", nullable = false)
    var aprvSt: String? = null

    @Basic
    @Column(name = "bldg_id", nullable = true)
    var bldgId: String? = null

    @Basic
    @Column(name = "co_id", nullable = true)
    var coId: String? = null

    @Basic
    @Column(name = "dismnt_dtm", nullable = true)
    var dismntDtm: String? = null

    @Basic
    @Column(name = "evet_rcv_yn", nullable = false)
    var evetRcvYn: Int? = null

    @Basic
    @Column(name = "fac_add_info", nullable = true)
    var facAddInfo: String? = null

    @Basic
    @Column(name = "fac_clfy_id", nullable = false)
    var facClfyId: String? = null

    @Basic
    @Column(name = "fac_desc", nullable = true)
    var facDesc: String? = null

    @Basic
    @Column(name = "fac_nm", nullable = false)
    var facNm: String? = null

    @Basic
    @Column(name = "fac_typ", nullable = false)
    var facTyp: String? = null

    @Basic
    @Column(name = "file_grup_id", nullable = true)
    var fileGrupId: String? = null

    @Basic
    @Column(name = "geo_ftr_cd", nullable = true)
    var geoFtrCd: String? = null

    @Basic
    @Column(name = "mfmn", nullable = true)
    var mfmn: String? = null

    @Basic
    @Column(name = "mgt_no", nullable = false)
    var mgtNo: String? = null

    @Basic
    @Column(name = "pos_grup_id", nullable = true)
    var posGrupId: String? = null

    @Basic
    @Column(name = "pos_nm", nullable = false)
    var posNm: String? = null

    @Basic
    @Column(name = "psst_period", nullable = false)
    var psstPeriod: Int? = null

    @Basic
    @Column(name = "reason", nullable = true)
    var reason: String? = null

    @Basic
    @Column(name = "reg_dtm", nullable = false)
    var regDtm: String? = null

    @Basic
    @Column(name = "regrt", nullable = false)
    var regrt: String? = null

    @Basic
    @Column(name = "setu_dtm", nullable = false)
    var setuDtm: String? = null

    @Basic
    @Column(name = "sevrty", nullable = true)
    var sevrty: String? = null

    @Basic
    @Column(name = "upd_dtm", nullable = true)
    var updDtm: String? = null

    @Basic
    @Column(name = "use_yn", nullable = false)
    var useYn: Int? = null

    @Basic
    @Column(name = "area", nullable = true)
    var area: String? = null

    @Basic
    @Column(name = "mobile_yn", nullable = true)
    var mobileYn: Int? = null

    @Basic
    @Column(name = "sync_yn", nullable = true)
    var syncYn: String? = "N"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fac_clfy_id", referencedColumnName = "fac_clfy_id", insertable = false, updatable = false)
    var refErfFacClfy: ErfFacClfy? = null

    @OneToMany(mappedBy = "refErfFacInfo", fetch = FetchType.LAZY)
    var refErfPosCrdnts: MutableList<ErfPosCrdnt>? = null

    @OneToMany(mappedBy = "refErfFacInfo", fetch = FetchType.LAZY)
    var refErfFacClfyAddInfoDatas: MutableList<ErfFacClfyAddInfoData>? = null

    @OneToMany(mappedBy = "facInfo", cascade = [CascadeType.ALL])
    private var facGrupMapps: List<ErfFacGrupMapp>? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "facId = $facId " +
                "addrLong = $addrLong " +
                "addrShort = $addrShort " +
                "aprvSt = $aprvSt " +
                "bldgId = $bldgId " +
                "coId = $coId " +
                "dismntDtm = $dismntDtm " +
                "evetRcvYn = $evetRcvYn " +
                "facAddInfo = $facAddInfo " +
                "facClfyId = $facClfyId " +
                "facDesc = $facDesc " +
                "facNm = $facNm " +
                "facTyp = $facTyp " +
                "fileGrupId = $fileGrupId " +
                "geoFtrCd = $geoFtrCd " +
                "mfmn = $mfmn " +
                "mgtNo = $mgtNo " +
                "posGrupId = $posGrupId " +
                "posNm = $posNm " +
                "psstPeriod = $psstPeriod " +
                "reason = $reason " +
                "regDtm = $regDtm " +
                "regrt = $regrt " +
                "setuDtm = $setuDtm " +
                "sevrty = $sevrty " +
                "updDtm = $updDtm " +
                "useYn = $useYn " +
                "area = $area " +
                "mobileYn = $mobileYn " +

                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ErfFacInfo

        if (facId != other.facId) return false
        if (addrLong != other.addrLong) return false
        if (addrShort != other.addrShort) return false
        if (aprvSt != other.aprvSt) return false
        if (bldgId != other.bldgId) return false
        if (coId != other.coId) return false
        if (dismntDtm != other.dismntDtm) return false
        if (evetRcvYn != other.evetRcvYn) return false
        if (facAddInfo != other.facAddInfo) return false
        if (facClfyId != other.facClfyId) return false
        if (facDesc != other.facDesc) return false
        if (facNm != other.facNm) return false
        if (facTyp != other.facTyp) return false
        if (fileGrupId != other.fileGrupId) return false
        if (geoFtrCd != other.geoFtrCd) return false
        if (mfmn != other.mfmn) return false
        if (mgtNo != other.mgtNo) return false
        if (posGrupId != other.posGrupId) return false
        if (posNm != other.posNm) return false
        if (psstPeriod != other.psstPeriod) return false
        if (reason != other.reason) return false
        if (regDtm != other.regDtm) return false
        if (regrt != other.regrt) return false
        if (setuDtm != other.setuDtm) return false
        if (sevrty != other.sevrty) return false
        if (updDtm != other.updDtm) return false
        if (useYn != other.useYn) return false
        if (area != other.area) return false
        if (mobileYn != other.mobileYn) return false
        if (syncYn != other.syncYn) return false

        return true
    }

}

