package com.eseict.apiServer.domain.rino.hams

import com.eseict.apiServer.conf.ApiConstants
import org.springframework.format.annotation.DateTimeFormat
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "tb_hams_seq", schema = ApiConstants.DB_SCHEMA.HS_S_SERVICE)
class TbHamsSeq(
    @Id
    @Column(name = "id", nullable = false)
    var id: String? = null,

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = ApiConstants.DATE.FORMAT_14)
    @Column(name = "send_date", nullable = false)
    var sendDate: Date? = null,

    @Basic
    @Column(name = "seq", nullable = true)
    var seq: Int? = null,

    @Basic
    @Column(name = "message", nullable = true)
    var message: String? = null

) {


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "date = $sendDate " +
                "seq = $seq " +
                "message = $message " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TbHamsSeq

        if (id != other.id) return false
        if (sendDate != other.sendDate) return false
        if (seq != other.seq) return false
        if (message != other.message) return false

        return true
    }

}

