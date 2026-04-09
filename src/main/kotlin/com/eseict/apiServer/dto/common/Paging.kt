package com.eseict.apiServer.dto.common

import com.eseict.apiServer.dto.GisFacInfo

data class Paging<T>(val total: Int, val data: List<T>?) {}
