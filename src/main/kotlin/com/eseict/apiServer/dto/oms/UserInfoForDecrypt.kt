package com.eseict.apiServer.dto.oms

data class UserInfoForDecrypt(
    var userId: String,
    var userLoginId: String,
    var userName: String,
    var cpNo: String?,
    var email: String?,
    var departmentId: String?,
    var departmentName: String?,
    var encryptKey: String
)