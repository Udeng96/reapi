package com.eseict.apiServer.domain.xeus

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "mt_sys_prop", schema = ApiConstants.DB_SCHEMA.XEUS)
class MtSysProp {

    @Id
    @Basic
    @Column(name = "prop_key", nullable = false)
    var propKey: String? = null

    @Basic
    @Column(name = "prop_nm", nullable = true)
    var propNm: String? = null

    @Basic
    @Column(name = "prop_value", nullable = false)
    var propValue: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "propKey = $propKey " +
                "propNm = $propNm " +
                "propValue = $propValue " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MtSysProp

        if (propKey != other.propKey) return false
        if (propNm != other.propNm) return false
        if (propValue != other.propValue) return false

        return true
    }

}

