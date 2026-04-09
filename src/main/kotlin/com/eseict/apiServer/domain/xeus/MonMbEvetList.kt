package com.eseict.apiServer.domain.xeus

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "mon_mb_evet_list", schema = ApiConstants.DB_SCHEMA.XEUS)
class MonMbEvetList {
    @Id
    @Column(name = "vod_seq", nullable = false)
    var vodSeq: Int? = null

    @Basic
    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Basic
    @Column(name = "vod_name", nullable = false)
    var vodName: String? = null

    @Basic
    @Column(name = "vod_stat_dtm", nullable = false)
    var vodStatDtm: String? = null

    @Basic
    @Column(name = "vod_end_dtm", nullable = false)
    var vodEndDtm: String? = null

    @Basic
    @Column(name = "vod_all_time", nullable = true)
    var vodAllTime: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "vodSeq = $vodSeq " +
                "userId = $userId " +
                "vodName = $vodName " +
                "vodStatDtm = $vodStatDtm " +
                "vodEndDtm = $vodEndDtm " +
                "vodAllTime = $vodAllTime " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MonMbEvetList

        if (vodSeq != other.vodSeq) return false
        if (userId != other.userId) return false
        if (vodName != other.vodName) return false
        if (vodStatDtm != other.vodStatDtm) return false
        if (vodEndDtm != other.vodEndDtm) return false
        if (vodAllTime != other.vodAllTime) return false

        return true
    }

}

