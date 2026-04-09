package com.eseict.apiServer.dto.fireResponder

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class ChannelResult(
    @JacksonXmlProperty(localName = "ChannelID")
    var channelId: String,
    var station: String,
    var car: String,
    var carnum: String,
    var connect: String,
    @JacksonXmlProperty(localName = "RTSP_ADDR")
    var rtspAddr: String
) {

}
