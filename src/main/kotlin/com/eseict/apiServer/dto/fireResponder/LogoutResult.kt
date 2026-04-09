package com.eseict.apiServer.dto.fireResponder

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class LogoutResult(
    @JacksonXmlProperty(localName = "Result")
    var result: String
) {
}
