package com.eseict.apiServer.util

import org.apache.commons.codec.binary.Base64
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class Aes128(var ips:String?, var keySpec :Key?) {

    private final val logger = LoggerFactory.getLogger(Aes128::class.java)

    constructor(key:String) : this(key.substring(0, 16),null) {
        try {
            val keyBytes = ByteArray(16)
            val b = key.toByteArray(charset("UTF-8"))
            System.arraycopy(b, 0, keyBytes, 0, keyBytes.size)
            val keySpec = SecretKeySpec(keyBytes, "AES")

            this.keySpec = keySpec
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun encrypt(str: String): String? {

        return try {

//            var cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            var cipher: Cipher = Cipher.getInstance("AES")
//            cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(ips!!.toByteArray(charset("UTF-8"))) as AlgorithmParameterSpec)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)

            val encrypted: ByteArray = cipher.doFinal(str.toByteArray(charset("UTF-8")))
            String(Base64.encodeBase64(encrypted))

        } catch (e: Exception) {
            null
        }
    }


    fun decrypt(str: String): String? {
        return try {
//            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val cipher = Cipher.getInstance("AES")
            cipher.init(
                Cipher.DECRYPT_MODE, keySpec,
//                IvParameterSpec(ips!!.toByteArray(charset("UTF-8")))
            )
            val byteStr: ByteArray = Base64.decodeBase64(str.toByteArray(Charset.forName("UTF-8")))
            String(cipher.doFinal(byteStr), Charset.forName("UTF-8"))

        } catch (e: Exception) {
            null
        }
    }

}