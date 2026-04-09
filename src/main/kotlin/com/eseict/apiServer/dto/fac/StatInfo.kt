package com.eseict.apiServer.dto.fac

import java.io.Serializable

data class StatInfo<T>(
    var fac: GisInfo,
    var data: List<T>?
) : Serializable