package com.eseict.apiServer.repository.rino

import com.eseict.apiServer.repository.RepositorySupport
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import java.lang.reflect.ParameterizedType
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

abstract class RinoRepositorySupport<T> : RepositorySupport<T> {
    internal final val LOGGER = LoggerFactory.getLogger(RinoRepositorySupport::class.java)

    //상속받는 repository 마다 emf 를 주입해주는 것은 너무 귀찮고!, 별 문제가 없을거라 생각이 들어서 autowired 를 사용함 추후 문제시 변경!
    @Autowired
    private lateinit var emf: EntityManagerFactory

    constructor() {
        super.persistentClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }

    override fun getEntityManager(): EntityManager {
        return emf.createEntityManager()
    }
}