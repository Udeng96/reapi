package com.eseict.apiServer.conf.db

import com.eseict.apiServer.conf.DataContextEnum

class DataContextHolder {
    companion object {
        private var context: DataContextEnum = DataContextEnum.RINO

        fun setDataContext(dataContext: DataContextEnum) {
            context = dataContext
        }

        fun getDataContext(): DataContextEnum {
            return context
        }

        fun clearDataContext() {
        }
    }
}