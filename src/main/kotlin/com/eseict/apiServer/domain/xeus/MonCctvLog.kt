package com.eseict.apiServer.domain.xeus

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "mon_cctv_log", schema = ApiConstants.DB_SCHEMA.XEUS)
class MonCctvLog {

    @Id
    @Basic
    @Column(name = "mgr_seq", nullable = false)
    var mgrSeq: Long? = null

    @Basic
    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Basic
    @Column(name = "cctv_mgr_no", nullable = false)
    var cctvMgrNo: String? = null

    @Basic
    @Column(name = "use_typ", nullable = true)
    var useTyp: String? = null

    @Basic
    @Column(name = "start_dat", nullable = true)
    var startDat: String? = null

    @Basic
    @Column(name = "end_dat", nullable = true)
    var endDat: String? = null

    @Basic
    @Column(name = "nonce", nullable = true)
    var nonce: String? = null

    @Basic
    @Column(name = "con_ip", nullable = true)
    var conIp: String? = null

    @Basic
    @Column(name = "note", nullable = true)
    var note: String? = null

    @Basic
    @Column(name = "view_size", nullable = true)
    var viewSize: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "mgrSeq = $mgrSeq " +
                "userId = $userId " +
                "cctvMgrNo = $cctvMgrNo " +
                "useTyp = $useTyp " +
                "startDat = $startDat " +
                "endDat = $endDat " +
                "nonce = $nonce " +
                "conIp = $conIp " +
                "note = $note " +
                "viewSize = $viewSize " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MonCctvLog

        if (mgrSeq != other.mgrSeq) return false
        if (userId != other.userId) return false
        if (cctvMgrNo != other.cctvMgrNo) return false
        if (useTyp != other.useTyp) return false
        if (startDat != other.startDat) return false
        if (endDat != other.endDat) return false
        if (nonce != other.nonce) return false
        if (conIp != other.conIp) return false
        if (note != other.note) return false
        if (viewSize != other.viewSize) return false

        return true
    }

}

