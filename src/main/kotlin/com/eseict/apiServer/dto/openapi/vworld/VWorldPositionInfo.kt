package com.eseict.apiServer.dto.openapi.vworld

data class VWorldPositionInfo(
    var id: String,
    var title:String,
    var address: Address,
    var point: Point
) {

    data class Address(
        var zipcode:String,
        var category: String,
        var road: String,
        var parcel: String,
        var bldnm: String,
        var bldnmdc:String,
    )

    data class Point(
        var x: String,
        var y: String
    )


}
