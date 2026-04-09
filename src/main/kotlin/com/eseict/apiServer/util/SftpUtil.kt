package com.eseict.apiServer.util

import com.google.common.base.Strings
import com.jcraft.jsch.*
import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.util.*

class SftpUtil {

    private val PATHSEPARATOR = "/"
    private val logger = LoggerFactory.getLogger(SftpUtil::class.java)

    private val sourcePath = ""
    private val dir = "/home/rino/rino/files"
    private var sftp: ChannelSftp? = null
    private var session: Session? = null


    fun connectSftpServer(sftpUser: String?, sftpIp: String?, sftpPort: Int, sftpPw: String?): Boolean {
        return try {
            val jsch = JSch()
            session = jsch.getSession(sftpUser, sftpIp, sftpPort)
            session!!.setPassword(sftpPw)
            val config = Properties()
            config["StrictHostKeyChecking"] = "no"
            session!!.setConfig(config)
            session!!.connect()
            val channel: Channel = session!!.openChannel("sftp")
            sftp = channel as ChannelSftp
            sftp!!.connect()
            true
        } catch (e: JSchException) {
            logger.error(e.message, e)
            false
        } catch (se: Exception) {
            logger.error(se.message, se)
            false
        }
    }

    fun disconnectSftpServer(): Boolean {
        var result = false
        session!!.disconnect()
        sftp!!.disconnect()
        result = true
        return result
    }

    fun uploadFile(saveFile: File, saveDir: String?): Boolean {
        var saveDir = saveDir
        var result = false
        var fis: FileInputStream? = null
        try {
            if (Strings.isNullOrEmpty(saveDir)) {
                saveDir = dir
            }
            fis = FileInputStream(saveFile)
            try {
                sftp!!.cd(saveDir)
            } catch (se: SftpException) {
                sftp!!.mkdir(saveDir)
                sftp!!.cd(saveDir)
            }
            sftp!!.put(fis, saveFile.name)
            result = true
        } catch (e: FileNotFoundException) {
            logger.error(e.message, e)
        } catch (e: SftpException) {
            logger.error(e.message, e)
        } finally {
            try {
                fis?.close()
            } catch (e: IOException) {
                logger.error(e.message, e)
            }
        }
        return result
    }

    fun removeFile(fileName: String, saveDir: String): Boolean {
        var saveDir = saveDir
        var result = false
        try {
            if (Strings.isNullOrEmpty(saveDir)) {
                saveDir = dir
            }
            sftp!!.rm(saveDir + PATHSEPARATOR + fileName)
            result = true
        } catch (e: SftpException) {
            logger.error(e.message, e)
        } finally {
        }
        return result
    }

    fun downloadFile(fileName: String?, saveDir: String?): File? {
        var fileName = fileName
        var saveDir = saveDir
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        val buffer = ByteArray(1024)
        var downloadFile: File? = null
        try {
            if (Strings.isNullOrEmpty(saveDir)) {
                saveDir = dir
            }
            try {
                sftp!!.cd(saveDir)
            } catch (se: SftpException) {
                sftp!!.mkdir(saveDir)
                sftp!!.cd(saveDir)
            }
            if (fileName != null && "" != fileName) {
                fileName = fileName.replace("/".toRegex(), "")
                //                fileName = fileName.replaceAll("\\", "");
                fileName = fileName.replace(".".toRegex(), "")
                fileName = fileName.replace("&".toRegex(), "")
            }
            val file = File(fileName)
            var newFileName = file.name
            if (newFileName != null && "" != newFileName) {
                newFileName = newFileName.replace("/".toRegex(), "")
                //                newFileName = newFileName.replaceAll("\\", "");
                newFileName = newFileName.replace(".".toRegex(), "")
                newFileName = newFileName.replace("&".toRegex(), "")
            }
            bis = BufferedInputStream(sftp!!.get(newFileName))
            val downlodDir: File = File(sourcePath)
            if (!downlodDir.exists()) {
                downlodDir.mkdirs()
            }
            downloadFile = File(sourcePath + newFileName)
            val os: OutputStream = FileOutputStream(downloadFile)
            bos = BufferedOutputStream(os)
            var readCount: Int
            while (bis.read(buffer).also { readCount = it } > 0) {
                bos.write(buffer, 0, readCount)
            }
            logger.info("File downloaded successfully [{}] ", file.absolutePath)
        } catch (e: SftpException) {
            logger.error(e.message, e)
        } catch (e: FileNotFoundException) {
            logger.error(e.message, e)
        } catch (e: IOException) {
            logger.error(e.message, e)
        } finally {
            if (bis != null) {
                try {
                    bis.close()
                } catch (e: IOException) {
                    logger.error(e.message, e)
                }
            }
            if (bos != null) {
                try {
                    bos.close()
                } catch (e: IOException) {
                    logger.error(e.message, e)
                }
            }
        }
        return downloadFile
    }

    fun convert(file: MultipartFile): File? {
        var convFile: File? = null
        try {
            convFile = File(file.originalFilename)
            convFile.createNewFile()
            val fos = FileOutputStream(convFile)
            fos.write(file.bytes)
            fos.close()
        } catch (e: IOException) {
            logger.error(e.message)
        }
        return convFile
    }
}