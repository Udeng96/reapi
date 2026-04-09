package com.eseict.apiServer.domain.hams

import com.eseict.apiServer.conf.ApiConstants
import com.eseict.apiServer.domain.rino.fms.ErfFacClfyAddInfoData
import com.eseict.apiServer.domain.rino.fms.ErfFacInfo
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "tb_equipment_data", schema = ApiConstants.DB_SCHEMA.HAMS)
//@Table(name="tb_equipment_data", schema = "hams_gl")
class TbEquipmentData {

    @Id
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Basic
    @Column(name = "NAME", nullable = true)
    var name: String? = null

    @Basic
    @Column(name = "LATITUDE", nullable = true)
    var latitude: BigDecimal? = null

    @Basic
    @Column(name = "LONGITUDE", nullable = true)
    var longitude: BigDecimal? = null

    @Basic
    @Column(name = "IP", nullable = true)
    var ip: String? = null

    @Basic
    @Column(name = "PORT", nullable = true)
    var port: Long? = null


    @Basic
    @Column(name = "JIBUN_ADDRESS", nullable = true)
    var jibunAddress: String? = null

    @Basic
    @Column(name = "ROADNAME_ADDRESS", nullable = true)
    var roadnameAddress: String? = null

    @Basic
    @Column(name = "EXTRA_ADDRESS", nullable = true)
    var extraAddress: String? = null

//    @Basic
//    @Column(name = "region_code", nullable = true)
//    var regionCode: String? = null

    @Basic
    @Column(name = "group_id", nullable = true, insertable = false, updatable = false)
    var groupId: Long? = null
//    @Basic
//    @Column(name = "type_id", nullable = true)
//    var typeId: Long? = null

    @Basic
    @Column(name = "jibun_address_id", nullable = true)
    var jibunAddressId: Long? = null

    @Basic
    @Column(name = "roadname_address_id", nullable = true)
    var roadnameAddressId: Long? = null

    @Basic
    @Column(name = "dc_1_label", nullable = true)
    var dc1Label: String? = null

    @Basic
    @Column(name = "dc_2_label", nullable = true)
    var dc2Label: String? = null

    @Basic
    @Column(name = "power_1_label", nullable = true)
    var power1Label: String? = null

    @Basic
    @Column(name = "power_2_label", nullable = true)
    var power2Label: String? = null

    @Basic
    @Column(name = "power_3_label", nullable = true)
    var power3Label: String? = null

    @Basic
    @Column(name = "power_4_label", nullable = true)
    var power4Label: String? = null

    @Basic
    @Column(name = "power_5_label", nullable = true)
    var power5Label: String? = null

    @Basic
    @Column(name = "note", nullable = true)
    var note: String? = null

    @Basic
    @Column(name = "usable", nullable = true)
    var usable: String? = null

    @Basic
    @Column(name = "setup_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = ApiConstants.DATE.FORMAT_14)
    var setupDate: Date? = null

    @Basic
    @Column(name = "modified_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = ApiConstants.DATE.FORMAT_14)
    var modifiedDate: Date? = null
//
//    @Basic
//    @Column(name = "mode", nullable = true)
//    var mode: String? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    var refTbGroupData: TbGroupData? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "name = $name " +
                "latitude = $latitude " +
                "longitude = $longitude " +
                "ip = $ip " +
                "port = $port " +
                "jibunAddress = $jibunAddress " +
                "roadnameAddress = $roadnameAddress " +
                "extraAddress = $extraAddress " +
//                "regionCode = $regionCode " +
                "groupId = $groupId " +
//                "typeId = $typeId " +
                "jibunAddressId = $jibunAddressId " +
                "roadnameAddressId = $roadnameAddressId " +
                "dc1Label = $dc1Label " +
                "dc2Label = $dc2Label " +
                "power1Label = $power1Label " +
                "power2Label = $power2Label " +
                "power3Label = $power3Label " +
                "power4Label = $power4Label " +
                "power5Label = $power5Label " +
                "note = $note " +
                "usable = $usable " +
                "setupDate = $setupDate " +
                "modifiedDate = $modifiedDate " +
//                "mode = $mode " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TbEquipmentData

        if (id != other.id) return false
        if (name != other.name) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (ip != other.ip) return false
        if (port != other.port) return false
        if (jibunAddress != other.jibunAddress) return false
        if (roadnameAddress != other.roadnameAddress) return false
        if (extraAddress != other.extraAddress) return false
//        if (regionCode != other.regionCode) return false
        if (groupId != other.groupId) return false
//        if (typeId != other.typeId) return false
        if (jibunAddressId != other.jibunAddressId) return false
        if (roadnameAddressId != other.roadnameAddressId) return false
        if (dc1Label != other.dc1Label) return false
        if (dc2Label != other.dc2Label) return false
        if (power1Label != other.power1Label) return false
        if (power2Label != other.power2Label) return false
        if (power3Label != other.power3Label) return false
        if (power4Label != other.power4Label) return false
        if (power5Label != other.power5Label) return false
        if (note != other.note) return false
        if (usable != other.usable) return false
        if (setupDate != other.setupDate) return false
        if (modifiedDate != other.modifiedDate) return false
//        if (mode != other.mode) return false

        return true
    }

    fun compareFac(other: ErfFacInfo): Boolean {

        var result = true

        if ("HAMS_" + id != other.mgtNo) result = false
        if (name != other.facNm) result = false
        if (!(roadnameAddress != other.addrLong || roadnameAddress != other.addrShort || roadnameAddress != other.posNm)) result =
            false

        if (other.refErfPosCrdnts?.size!! > 0) {
            if (latitude.toString() != other.refErfPosCrdnts?.get(0)?.YCrdnt) result = false
            if (longitude.toString() != other.refErfPosCrdnts?.get(0)?.XCrdnt) result = false
        }
//        if (ip != this.시설물에있는IP꺼내기(other.refErfFacClfyAddInfoDatas) ?: "") result= false
//        if (port != this.시설물에있는PORT꺼내기(other.refErfFacClfyAddInfoDatas) ?: "") result= false

        return result
    }

    private fun 시설물에있는IP꺼내기(list: MutableList<ErfFacClfyAddInfoData>?): String? =
        list?.findLast { it.addInfoId == "HAMS_IP" }?.addInfoData

    private fun 시설물에있는PORT꺼내기(list: MutableList<ErfFacClfyAddInfoData>?): String? =
        list?.findLast { it.addInfoId == "HAMS_PORT" }?.addInfoData
}

