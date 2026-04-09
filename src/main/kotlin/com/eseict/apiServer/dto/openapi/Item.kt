package com.eseict.apiServer.dto.openapi

import java.io.Serializable

data class Item<T>(
    val item: List<T>
) : Serializable