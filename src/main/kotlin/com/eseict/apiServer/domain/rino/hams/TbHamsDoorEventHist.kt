package com.eseict.apiServer.domain.rino.hams

import com.eseict.apiServer.conf.ApiConstants
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "tb_hams_door_event_hist", schema = ApiConstants.DB_SCHEMA.HS_S_SERVICE)
class TbHamsDoorEventHist {
    @Id
    @Column(name = "id", nullable = false)
    var id: String? = null

    @Basic
    @Column(name = "eq_id", nullable = false)
    var eqId: String? = null


    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = ApiConstants.DATE.FORMAT_14)
    @Column(name = "occur_date", nullable = false)
    var occurDate: Date? = null

    @Basic
    @Column(name = "orderer_type", nullable = true)
    var ordererType: String? = "0"

    @Basic
    @Column(name = "user_id", nullable = true)
    var userId: String? = "0"

    @Basic
    @Column(name = "orderer_ip", nullable = true)
    var ordererIp: String? = "0"

    @Basic
    @Column(name = "door_type", nullable = false)
    var doorType: String? = null //앞문 뒷문 구분

    @Basic
    @Column(name = "occur_target", nullable = false)
    var occurTarget: String? = null  //도어 , 도어락 구분

    @Basic
    @Column(name = "event_type", nullable = true)
    var eventType: String? = null   //이벤트 값

    constructor(
        id: String?,
        eqId: String?,
        occurDate: Date?,
        doorType: String?,
        occurTarget: String?,
        eventType: String?
    ) {
        this.id = id
        this.eqId = eqId
        this.occurDate = occurDate
        this.doorType = doorType
        this.occurTarget = occurTarget
        this.eventType = eventType
    }


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "eqId = $eqId " +
                "occurDate = $occurDate " +
                "ordererType = $ordererType " +
                "userId = $userId " +
                "ordererIp = $ordererIp " +
                "doorType = $doorType " +
                "occurTarget = $occurTarget " +
                "eventType = $eventType " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TbHamsDoorEventHist

        if (id != other.id) return false
        if (eqId != other.eqId) return false
        if (occurDate != other.occurDate) return false
        if (ordererType != other.ordererType) return false
        if (userId != other.userId) return false
        if (ordererIp != other.ordererIp) return false
        if (doorType != other.doorType) return false
        if (occurTarget != other.occurTarget) return false
        if (eventType != other.eventType) return false

        return true
    }

}

