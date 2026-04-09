package com.eseict.apiServer.domain.rino.fms

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "erf_fac_clfy", schema = ApiConstants.DB_SCHEMA.FMS)
class ErfFacClfy {
    @Id
    @Column(name = "fac_clfy_id", nullable = false, insertable = false, updatable = false)
    var facClfyId: String? = null

    @Basic
    @Column(name = "fac_clfy_gb", nullable = false)
    var facClfyGb: Char? = null

    @Basic
    @Column(name = "fac_clfy_nm", nullable = false)
    var facClfyNm: String? = null

    @Basic
    @Column(name = "fac_clfy_path", nullable = true)
    var facClfyPath: String? = "#"

    @Basic
    @Column(name = "file_id", nullable = true)
    var fileId: String? = null

    @Basic
    @Column(name = "lvl_no", nullable = false)
    var lvlNo: Int? = null

    @Basic
    @Column(name = "mfmn", nullable = true)
    var mfmn: String? = null

    @Basic
    @Column(name = "note", nullable = true)
    var note: String? = null

    @Basic
    @Column(name = "reg_dtm", nullable = false)
    var regDtm: String? = null

    @Basic
    @Column(name = "regrt", nullable = false)
    var regrt: String? = null

    @Basic
    @Column(name = "top_fac_clfy_id", nullable = false)
    var topFacClfyId: String? = null

    @Basic
    @Column(name = "upd_dtm", nullable = true)
    var updDtm: String? = null

    @Basic
    @Column(name = "use_yn", nullable = false)
    var useYn: Int? = null

    @Basic
    @Column(name = "fac_clfy_ico", nullable = true)
    var facClfyIco: ByteArray? = null

    @OneToMany(mappedBy = "refErfFacClfy")
    var refErfFacInfos: List<ErfFacInfo>? = null

    constructor(
        facClfyId: String?, facClfyGb: Char?, facClfyNm: String?, lvlNo: Int?,
        regDtm: String?, regrt: String?, topFacClfyId: String?, useYn: Int?
    ) {
        this.facClfyId = facClfyId
        this.facClfyGb = facClfyGb
        this.facClfyNm = facClfyNm
        this.lvlNo = lvlNo
        this.regDtm = regDtm
        this.regrt = regrt
        this.topFacClfyId = topFacClfyId
        this.useYn = useYn
    }


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "facClfyId = $facClfyId " +
                "facClfyGb = $facClfyGb " +
                "facClfyNm = $facClfyNm " +
                "facClfyPath = $facClfyPath " +
                "fileId = $fileId " +
                "lvlNo = $lvlNo " +
                "mfmn = $mfmn " +
                "note = $note " +
                "regDtm = $regDtm " +
                "regrt = $regrt " +
                "topFacClfyId = $topFacClfyId " +
                "updDtm = $updDtm " +
                "useYn = $useYn " +
                "facClfyIco = $facClfyIco " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ErfFacClfy

        if (facClfyId != other.facClfyId) return false
        if (facClfyGb != other.facClfyGb) return false
        if (facClfyNm != other.facClfyNm) return false
        if (facClfyPath != other.facClfyPath) return false
        if (fileId != other.fileId) return false
        if (lvlNo != other.lvlNo) return false
        if (mfmn != other.mfmn) return false
        if (note != other.note) return false
        if (regDtm != other.regDtm) return false
        if (regrt != other.regrt) return false
        if (topFacClfyId != other.topFacClfyId) return false
        if (updDtm != other.updDtm) return false
        if (useYn != other.useYn) return false
        if (facClfyIco != other.facClfyIco) return false


        return true
    }

}

