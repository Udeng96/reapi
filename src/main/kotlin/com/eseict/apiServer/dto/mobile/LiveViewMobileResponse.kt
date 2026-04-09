package com.eseict.apiServer.dto.mobile

import com.eseict.apiServer.domain.eas.LiveViewMobileInfo

data class LiveViewMobileResponse(
    val result: String? = null,
    val notice: String? = null,
    var mobileList: List<LiveViewMobileInfo?>?
) {
    constructor() : this(mobileList = null)
}
