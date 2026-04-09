package com.eseict.apiServer.domain.xeus

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "mon_mb_report_data", schema = ApiConstants.DB_SCHEMA.XEUS)
class MonMbReportData {
    @Id
    @Column(name = "mb_seq", nullable = false)
    var mbSeq: Int? = null

    @Basic
    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Basic
    @Column(name = "rpt_dtm", nullable = false)
    var rptDtm: String? = null

    @Basic
    @Column(name = "rpt_nm", nullable = false)
    var rptNm: String? = null

    @Basic
    @Column(name = "rpt_cntn", nullable = false)
    var rptCntn: String? = null

    @Basic
    @Column(name = "rpt_image", nullable = true)
    var rptImage: String? = null

    @Basic
    @Column(name = "rpt_image_path", nullable = true)
    var rptImagePath: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "mbSeq = $mbSeq " +
                "userId = $userId " +
                "rptDtm = $rptDtm " +
                "rptNm = $rptNm " +
                "rptCntn = $rptCntn " +
                "rptImage = $rptImage " +
                "rptImagePath = $rptImagePath " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MonMbReportData

        if (mbSeq != other.mbSeq) return false
        if (userId != other.userId) return false
        if (rptDtm != other.rptDtm) return false
        if (rptNm != other.rptNm) return false
        if (rptCntn != other.rptCntn) return false
        if (rptImage != other.rptImage) return false
        if (rptImagePath != other.rptImagePath) return false

        return true
    }

}

