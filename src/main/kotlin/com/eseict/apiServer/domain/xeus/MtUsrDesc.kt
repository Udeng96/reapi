package com.eseict.apiServer.domain.xeus

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "mt_usr_desc", schema = ApiConstants.DB_SCHEMA.XEUS)
class MtUsrDesc {
    @Id
    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Basic
    @Column(name = "user_name", nullable = false)
    var userName: String? = null

    @Basic
    @Column(name = "user_pwd", nullable = true)
    var userPwd: String? = null

    @Basic
    @Column(name = "auth_grp_no", nullable = true)
    var authGrpNo: String? = null

    @Basic
    @Column(name = "org_mgr_no", nullable = true)
    var orgMgrNo: String? = null

    @Basic
    @Column(name = "tel_num", nullable = true)
    var telNum: String? = null

    @Basic
    @Column(name = "mobile_num", nullable = true)
    var mobileNum: String? = null

    @Basic
    @Column(name = "depart_nm", nullable = true)
    var departNm: String? = null

    @Basic
    @Column(name = "pos_nm", nullable = true)
    var posNm: String? = null

    @Basic
    @Column(name = "email", nullable = true)
    var email: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "userId = $userId " +
                "userName = $userName " +
                "userPwd = $userPwd " +
                "authGrpNo = $authGrpNo " +
                "orgMgrNo = $orgMgrNo " +
                "telNum = $telNum " +
                "mobileNum = $mobileNum " +
                "departNm = $departNm " +
                "posNm = $posNm " +
                "email = $email " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MtUsrDesc

        if (userId != other.userId) return false
        if (userName != other.userName) return false
        if (userPwd != other.userPwd) return false
        if (authGrpNo != other.authGrpNo) return false
        if (orgMgrNo != other.orgMgrNo) return false
        if (telNum != other.telNum) return false
        if (mobileNum != other.mobileNum) return false
        if (departNm != other.departNm) return false
        if (posNm != other.posNm) return false
        if (email != other.email) return false

        return true
    }

}

