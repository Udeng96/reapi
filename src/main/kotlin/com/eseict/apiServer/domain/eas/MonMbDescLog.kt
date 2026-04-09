package com.eseict.apiServer.domain.eas

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "mon_mb_desc_log", schema = ApiConstants.DB_SCHEMA.EAS)
@SequenceGenerator(
    name = "MON_MB_DESC_LOG_GID_GENERATOR",
    sequenceName = "mon_mb_desc_log__gid_seq", // 매핑할 데이터베이스 시퀀스 이름
    initialValue = 1,
    allocationSize = 1
)
class MonMbDescLog(
    @Id
    @Column(name = "mb_seq", nullable = false, insertable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MON_MB_DESC_LOG_SEQ_GENERATOR")
//    @SequenceGenerator(
//        name = "MON_MB_DESC_LOG_SEQ_GENERATOR",
//        sequenceName = "mon_mb_desc_log_mb_seq", // 매핑할 데이터베이스 시퀀스 이름
//        initialValue = 1,
//        allocationSize = 1
//    )
    var mbSeq: Int? = null,

    @Basic
    @Column(name = "user_id", nullable = false)
    var userId: String? = null,

    @Column(name = "user_nm", nullable = false)
    var userNm: String? = null,

    @Basic
    @Column(name = "user_dtm", nullable = false)
    var userDtm: String? = null,

    @Basic
    @Column(name = "lat", nullable = false)
    var lat: Double? = null,

    @Basic
    @Column(name = "lon", nullable = false)
    var lon: Double? = null,

    @Basic
    @Column(name = "_gid", nullable = false, columnDefinition = "serial", insertable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MON_MB_DESC_LOG_GID_GENERATOR")
    var gid: Int? = null,

    @Basic
    @Column(name = "mb_chk", nullable = true)
    var mbChk: Int? = null,

    @Basic
    @Column(name = "mb_url", nullable = true)
    var mbUrl: String? = null,

    @Basic
    @Column(name = "live_chk", nullable = true)
    var liveChk: String? = null,

    @Basic
    @Column(name = "title", nullable = true)
    var title: String? = null,

    @Basic
    @Column(name = "dpt_nm", nullable = true)
    var dptNm: String? = null,

    @Basic
    @Column(name = "mgr_no", nullable = true) //전화번호
    var mgrNo: String? = null,
) {


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "mbSeq = $mbSeq " +
                "userId = $userId " +
                "userDtm = $userDtm " +
                "lat = $lat " +
                "lon = $lon " +
                "gid = $gid " +
                "mbChk = $mbChk " +
                "mbUrl = $mbUrl " +
                "liveChk = $liveChk " +
                "title = $title " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MonMbDescLog

        if (mbSeq != other.mbSeq) return false
        if (userId != other.userId) return false
        if (userDtm != other.userDtm) return false
        if (lat != other.lat) return false
        if (lon != other.lon) return false
        if (gid != other.gid) return false
        if (mbChk != other.mbChk) return false
        if (mbUrl != other.mbUrl) return false
        if (liveChk != other.liveChk) return false
        if (title != other.title) return false

        return true
    }

}

