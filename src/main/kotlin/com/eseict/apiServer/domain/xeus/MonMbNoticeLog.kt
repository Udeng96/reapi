package com.eseict.apiServer.domain.xeus

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "mon_mb_notice_log", schema = ApiConstants.DB_SCHEMA.XEUS)
class MonMbNoticeLog {
    @Id
    @Column(name = "mb_seq", nullable = false)
    var mbSeq: Int? = null

    @Basic
    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Basic
    @Column(name = "noti_dtm", nullable = false)
    var notiDtm: String? = null

    @Basic
    @Column(name = "noti_nm", nullable = true)
    var notiNm: String? = null

    @Basic
    @Column(name = "noti_chk", nullable = true)
    var notiChk: Int? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "mbSeq = $mbSeq " +
                "userId = $userId " +
                "notiDtm = $notiDtm " +
                "notiNm = $notiNm " +
                "notiChk = $notiChk " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MonMbNoticeLog

        if (mbSeq != other.mbSeq) return false
        if (userId != other.userId) return false
        if (notiDtm != other.notiDtm) return false
        if (notiNm != other.notiNm) return false
        if (notiChk != other.notiChk) return false

        return true
    }

}

