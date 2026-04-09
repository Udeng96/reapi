package com.eseict.apiServer

import com.eseict.apiServer.conf.ApSystemProperties
import com.eseict.apiServer.proc.hams.SocketServerHamsMain
import com.eseict.apiServer.proc.hams.client.HamsSendClient
import com.eseict.apiServer.proc.hams.netty.client.HamsDoeClient
import com.eseict.apiServer.proc.rino.SocketServerMain
import com.eseict.apiServer.proc.rino.msgProc.SocketMsgQueuePolling
import com.eseict.apiServer.proc.rino.webSocket.WebSocketServer
import com.eseict.apiServer.util.geoJson.GeoJsonCache
import com.eseict.common.log.LoggerManager
import com.eseict.common.web.WebApplicationContextUtil
import com.google.gson.JsonParser
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.MessageSource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext
import java.io.File
import java.io.FileReader
import java.io.IOException
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Component
class ServletInitializer(val apSystemProperties: ApSystemProperties) : SpringBootServletInitializer() {

    private val LOGGER: Logger = LoggerManager.getLogger(ServletInitializer::class.java)

    @Autowired
    var wac: WebApplicationContext? = null

    @Autowired
    private val messageSource: MessageSource? = null

    @Value("\${hams.address}")
    lateinit var hamsAddress: String;

    @Value("\${hams.port}")
    lateinit var hamsPort: String;




    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(ApiServerApplication::class.java)
    }

    @PostConstruct
    fun initialization() {

        //SET GEO JSON
        setUpGeoJsonData()



        HamsDoeClient.getInstance(hamsAddress, hamsPort.toInt()).run()



        //HAMS NETTY SERVER START
        SocketServerHamsMain.getInstance(apSystemProperties.getValue("port"))?.init()

        //MRS RECEIVE PORT
        SocketServerMain.getInstance(apSystemProperties.getValue("eventPort")!!).init()
        //WS OPEN
        WebSocketServer.getInstance(apSystemProperties.getValue("wsPort")!!).init()

        //MsgQueue 초기화 및 Polling Thread 시작

        //MsgQueue 초기화 및 Polling Thread 시작
        val msgPolling = SocketMsgQueuePolling()
        val msgPollingThread = Thread(msgPolling)
        msgPollingThread.start()

    }

    fun setUpGeoJsonData() {
        try {

            val homePath = System.getenv("API_HOME")
            val sb = StringBuilder(homePath)
            sb.append(File.separatorChar)
            sb.append("geojson")
            sb.append(File.separatorChar)
            sb.append("location.json")
            val resource: Resource = FileSystemResource(sb.toString()) //디플로이시 HOME 패스에서 컨피그 로딩
            val jsonElement = JsonParser.parseReader(FileReader(resource.file))
            val geoJsonObject = jsonElement.asJsonObject
            val featuresArray = geoJsonObject.getAsJsonArray("features")
            for (i in 0 until featuresArray.size()) {
                val properties = featuresArray[i].asJsonObject.getAsJsonObject("properties")
                //				JsonArray coordinateArray = featuresArray.get(i).getAsJsonObject().getAsJsonObject("geometry").getAsJsonArray("coordinates").get(0).getAsJsonArray().get(0).getAsJsonArray();
                val coordinateArray =
                    featuresArray[i].asJsonObject.getAsJsonObject("geometry").getAsJsonArray("coordinates")
                GeoJsonCache.getInstance().setUpPropData(properties["adm_nm"].asString, properties)
                GeoJsonCache.getInstance().setUpCoordData(properties["adm_nm"].asString, coordinateArray)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @PreDestroy
    fun destroy() {
        LOGGER.info("close module!!")
        SocketServerHamsMain.getInstance()?.close()

        val isConnectHamsSendClient = HamsSendClient.getInstance().isConnect()

        LOGGER.info("HAMS Client connect is {}", isConnectHamsSendClient)

        if (isConnectHamsSendClient) {
            HamsSendClient.getInstance().clear()
        }
    }


}
