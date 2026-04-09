package com.eseict.apiServer.conf.hams

class HamsConstants {

    companion object {
        const val START_MESSAGE_CHAR = "\$PGL"
        const val PACKET_VERSION = 1
        const val NUMBER_OF_DATA_FIELDS = 2
        const val START_TAIL_CHAR = "*"
        const val END_MESSAGE_CHAR = ""
    }

    object COMMAND {
        const val CONNECT = "CON"
        const val SETTING = "SET"
        const val ERROR = "ERR"
        const val REBOOT = "RBT"

        const val EQ_STATUS = "S"
        const val EQ_CONTROL = "C"
        const val EQ_CANCEL = "X"
        const val EQ_EVENT = "E"
    }

    //ACK 메세지는 COMMAND명령어 앞에 ACK CAHR이 붙는다.
    object ACK {
        const val ACK_CHAR = "A"
    }


    //장비 명령어는 명령할 대상(TARGET) + EQ_COMMAND 의 3자리 조합이다.
    object TARGET {
        const val DOORLOCK = "DO"               //possible command  C,X,S,E
        const val POWER = "PO"                  //possible command  C,X,S,E
        const val DC = "DC" //DC,DC 12V         //possible command  C,X,S,E
        const val UPPER_TEMPERATURE = "UT"      //possible command  C,X,S,E
        const val TEMPERATURE_AND_HUMI = "TH"   //possible command  S,E
    }


}