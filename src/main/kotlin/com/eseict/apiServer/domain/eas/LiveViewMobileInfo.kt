package com.eseict.apiServer.domain.eas

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "tbl_live_view_mobile_info", schema = ApiConstants.DB_SCHEMA.EAS)
class LiveViewMobileInfo(
    @Id
    @Column(name = "mgr_no", nullable = false)
    var mgrNo: String? = null,

    @Basic
    @Column(name = "_lat", nullable = true)
    var lat: Double? = null,

    @Basic
    @Column(name = "_lng", nullable = true)
    var lng: Double? = null,

    @Basic
    @Column(name = "mb_url", nullable = true)
    var mbUrl: String? = null,

    @Basic
    @Column(name = "mobile_check", nullable = true)
    var mobileCheck: String? = null,

    @Basic
    @Column(name = "live_title", nullable = true)
    var liveTitle: String? = null,

    @Basic
    @Column(name = "user_dtm", nullable = true)
    var userDtm: String? = null,

    @Basic
    @Column(name = "user_id", nullable = true)
    var userId: String? = null
) {


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "mgrNo = $mgrNo " +
                "lat = $lat " +
                "lng = $lng " +
                "mbUrl = $mbUrl " +
                "mobileCheck = $mobileCheck " +
                "liveTitle = $liveTitle " +
                "userDtm = $userDtm " +
                "userId = $userId " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as LiveViewMobileInfo

        if (mgrNo != other.mgrNo) return false
        if (lat != other.lat) return false
        if (lng != other.lng) return false
        if (mbUrl != other.mbUrl) return false
        if (mobileCheck != other.mobileCheck) return false
        if (liveTitle != other.liveTitle) return false
        if (userDtm != other.userDtm) return false
        if (userId != other.userId) return false

        return true
    }

}

