package com.eseict.apiServer.dto.fac

data class FacClfyImpl(
    override var id: String,
    override var nm: String,
    override var regDtm: String,
    override var facCount: Int
) : FacClfy {

    constructor(
        id: String,
        nm: String,
        regDtm: String,
        facCount: Long
    ) : this(id, nm, regDtm, facCount.toInt())
}
