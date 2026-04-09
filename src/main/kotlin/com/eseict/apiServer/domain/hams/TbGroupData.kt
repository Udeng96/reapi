package com.eseict.apiServer.domain.hams

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.domain.rino.fms.ErfFacGrupMapp
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.format.annotation.DateTimeFormat
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "tb_group_data", schema = ApiConstants.DB_SCHEMA.HAMS)
//@Table(name = "tb_equipment_group", schema = ApiConstants.DB_SCHEMA.HAMS)
class TbGroupData(
    @Id
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Basic
    @Column(name = "parent", nullable = true)
    var parent: Long? = null,

    @Basic
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Basic
    @Column(name = "usable", nullable = true)
    var usable: String? = null,

    @Basic
    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = ApiConstants.DATE.FORMAT_14)
    var createdDate: Date? = null,

    @Basic
    @Column(name = "modified_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = ApiConstants.DATE.FORMAT_14)
    var modifiedDate: Date? = null
) {

    @JsonIgnore
    @OneToMany(mappedBy = "refTbGroupData", cascade = [CascadeType.ALL])
    private var equipmentDatas: List<TbEquipmentData>? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "parent = $parent " +
                "name = $name " +
                "usable = $usable " +
                "createdDate = $createdDate " +
                "modifiedDate = $modifiedDate " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TbGroupData

        if (id != other.id) return false
        if (parent != other.parent) return false
        if (name != other.name) return false
        if (usable != other.usable) return false
        if (createdDate != other.createdDate) return false
        if (modifiedDate != other.modifiedDate) return false

        return true
    }

}

