package com.eseict.apiServer.proc

import com.eseict.apiServer.dto.event.GeneralMessage

interface SendProc {
    fun send(gm: GeneralMessage): Boolean
}