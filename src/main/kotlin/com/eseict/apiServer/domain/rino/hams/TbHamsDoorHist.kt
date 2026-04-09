package com.eseict.apiServer.domain.rino.hams

import com.eseict.apiServer.conf.ApiConstants
import org.springframework.format.annotation.DateTimeFormat
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "tb_hams_door_hist", schema = ApiConstants.DB_SCHEMA.HS_S_SERVICE)
class TbHamsDoorHist(
    @Id
    @Column(name = "id", nullable = false)
    var id: String? = null,

    @Basic
    @Column(name = "eq_id", nullable = false)
    var eqId: String? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = ApiConstants.DATE.FORMAT_14)
    @Column(name = "occur_date", nullable = false)
    var occurDate: Date? = null,

    @Basic
    @Column(name = "door_type", nullable = false)
    var doorType: Char? = null,

    @Basic
    @Column(name = "door_state", nullable = false)
    var doorState: Char? = null,

    @Basic
    @Column(name = "door_lock_state", nullable = true)
    var doorLockState: Char? = null
) {


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "eqId = $eqId " +
                "occurDate = $occurDate " +
                "doorType = $doorType " +
                "doorState = $doorState " +
                "doorLockState = $doorLockState " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TbHamsDoorHist

        if (id != other.id) return false
        if (eqId != other.eqId) return false
        if (occurDate != other.occurDate) return false
        if (doorType != other.doorType) return false
        if (doorState != other.doorState) return false
        if (doorLockState != other.doorLockState) return false

        return true
    }

}

