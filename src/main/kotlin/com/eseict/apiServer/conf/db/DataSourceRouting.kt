package com.eseict.apiServer.conf.db

import com.eseict.apiServer.conf.DataContextEnum
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import javax.sql.DataSource

class DataSourceRouting : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any {
        return DataContextHolder.getDataContext()
    }

    fun initDataSource(rinoDbSource: DataSource, fireStationDbSource: DataSource) {
        DataContextHolder.setDataContext(DataContextEnum.RINO)
        val dataSourceMap = mutableMapOf<Any, Any>()
        dataSourceMap[DataContextEnum.RINO] = rinoDbSource
        dataSourceMap[DataContextEnum.FIRE_STATION] = fireStationDbSource
        setTargetDataSources(dataSourceMap)
        setDefaultTargetDataSource(rinoDbSource)
    }

}