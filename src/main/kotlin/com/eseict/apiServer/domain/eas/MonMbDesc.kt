package com.eseict.apiServer.domain.eas

import com.eseict.apiServer.conf.ApiConstants
import lombok.AllArgsConstructor
import javax.persistence.*


@Entity
@Table(name = "mon_mb_desc", schema = ApiConstants.DB_SCHEMA.EAS)
@SequenceGenerator(
    name = "MON_MB_DESC_GENERATOR",
    sequenceName = "mon_mb_desc__gid_seq", // 매핑할 데이터베이스 시퀀스 이름
    initialValue = 1,
    allocationSize = 1)
class MonMbDesc(
    @Id
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
    @Column(name = "_gid", nullable = false,columnDefinition = "serial", insertable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MON_MB_DESC_GENERATOR")
    var gid: Int? = null,

    @Basic
    @Column(name = "mobile_chk", nullable = true)
    var mobileChk: Int? = null,

    @Basic
    @Column(name = "mb_url", nullable = true)
    var mbUrl: String? = null,

    @Basic
    @Column(name = "title", nullable = true)
    var title: String? = null,

    @Basic
    @Column(name = "dpt_nm", nullable = true)
    var dptNm: String? = null,

    @Basic
    @Column(name = "mgr_no", nullable = true) //전화번호
    var mgrNo: String? = null
) {


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "userId = $userId " +
                "userDtm = $userDtm " +
                "lat = $lat " +
                "lon = $lon " +
                "gid = $gid " +
                "mobileChk = $mobileChk " +
                "mbUrl = $mbUrl " +
                "title = $title " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MonMbDesc

        if (userId != other.userId) return false
        if (userDtm != other.userDtm) return false
        if (lat != other.lat) return false
        if (lon != other.lon) return false
        if (gid != other.gid) return false
        if (mobileChk != other.mobileChk) return false
        if (mbUrl != other.mbUrl) return false
        if (title != other.title) return false

        return true
    }

}

