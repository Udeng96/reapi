package com.eseict.apiServer.domain.rino.oms

import com.eseict.apiServer.conf.ApiConstants
import javax.persistence.*

@Entity
@Table(name = "oms_user_info", schema = ApiConstants.DB_SCHEMA.OMS)
class OmsUserInfoEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    var userId: String? = null

    @Basic
    @Column(name = "cp_no", nullable = true)
    var cpNo: String? = null

    @Basic
    @Column(name = "dpt_id", nullable = true)
    var dptId: String? = null

    @Basic
    @Column(name = "email", nullable = true)
    var email: String? = null

    @Basic
    @Column(name = "encrypt_key", nullable = true)
    var encryptKey: String? = null

    @Basic
    @Column(name = "event_role_id", nullable = true)
    var eventRoleId: String? = null

    @Basic
    @Column(name = "fist_reg_dtm", nullable = true)
    var fistRegDtm: String? = null

    @Basic
    @Column(name = "login_block_flag", nullable = true)
    var loginBlockFlag: String? = null

    @Basic
    @Column(name = "login_try_count", nullable = true)
    var loginTryCount: Long? = null

    @Basic
    @Column(name = "lst_login_dtm", nullable = true)
    var lstLoginDtm: String? = null

    @Basic
    @Column(name = "phone", nullable = true)
    var phone: String? = null

    @Basic
    @Column(name = "role_id", nullable = true)
    var roleId: String? = null

    @Basic
    @Column(name = "seoul_user_type", nullable = true)
    var seoulUserType: String? = null

    @Basic
    @Column(name = "seoul_user_zn", nullable = true)
    var seoulUserZn: String? = null

    @Basic
    @Column(name = "use_yn", nullable = true)
    var useYn: String? = null

    @Basic
    @Column(name = "user_login_id", nullable = true)
    var userLoginId: String? = null

    @Basic
    @Column(name = "user_login_pswd", nullable = true)
    var userLoginPswd: String? = null

    @Basic
    @Column(name = "user_nm", nullable = true)
    var userNm: String? = null


    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "userId = $userId " +
                "cpNo = $cpNo " +
                "dptId = $dptId " +
                "email = $email " +
                "encryptKey = $encryptKey " +
                "eventRoleId = $eventRoleId " +
                "fistRegDtm = $fistRegDtm " +
                "loginBlockFlag = $loginBlockFlag " +
                "loginTryCount = $loginTryCount " +
                "lstLoginDtm = $lstLoginDtm " +
                "phone = $phone " +
                "roleId = $roleId " +
                "seoulUserType = $seoulUserType " +
                "seoulUserZn = $seoulUserZn " +
                "useYn = $useYn " +
                "userLoginId = $userLoginId " +
                "userLoginPswd = $userLoginPswd " +
                "userNm = $userNm " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OmsUserInfoEntity

        if (userId != other.userId) return false
        if (cpNo != other.cpNo) return false
        if (dptId != other.dptId) return false
        if (email != other.email) return false
        if (encryptKey != other.encryptKey) return false
        if (eventRoleId != other.eventRoleId) return false
        if (fistRegDtm != other.fistRegDtm) return false
        if (loginBlockFlag != other.loginBlockFlag) return false
        if (loginTryCount != other.loginTryCount) return false
        if (lstLoginDtm != other.lstLoginDtm) return false
        if (phone != other.phone) return false
        if (roleId != other.roleId) return false
        if (seoulUserType != other.seoulUserType) return false
        if (seoulUserZn != other.seoulUserZn) return false
        if (useYn != other.useYn) return false
        if (userLoginId != other.userLoginId) return false
        if (userLoginPswd != other.userLoginPswd) return false
        if (userNm != other.userNm) return false

        return true
    }

}

