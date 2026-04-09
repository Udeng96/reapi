package com.eseict.apiServer.repository

import org.hibernate.Criteria
import org.slf4j.LoggerFactory
import javax.persistence.EntityExistsException
import javax.persistence.EntityManager
import javax.persistence.TransactionRequiredException
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

abstract class RepositorySupport<T> {
    private final val LOGGER = LoggerFactory.getLogger(RepositorySupport::class.java)

    open lateinit var persistentClass: Class<T>

    abstract fun getEntityManager(): EntityManager

    fun get(pk: Any): T? {
        val em = getEntityManager()
        try {
            return em.find(persistentClass, pk)
        } finally {
            em.close()
        }
    }

    fun getList(): List<T> {
        val em = getEntityManager()
        try {
            val cb = em.criteriaBuilder

            val cq: CriteriaQuery<T> = cb.createQuery(persistentClass)
            cq.from(persistentClass)
            val resultQuery: TypedQuery<T> = em.createQuery(cq)
            return resultQuery.resultList
        } finally {
            em.close()
        }
    }

    fun insert(obj: T) {
        val em = getEntityManager()
        try {
            val transaction = em.transaction
            try {
                transaction.begin()
                em.persist(obj)
                transaction.commit()
            } catch (e: Exception) {
                when (e) {
                    is IllegalAccessException, is EntityExistsException, is TransactionRequiredException -> {
                        LOGGER.error(e.message, e)
                        throw e
                    }
                    else -> throw e
                }
            } finally {
                if (transaction.isActive) {
                    transaction.rollback()
                }
            }
        } finally {
            em.close()
        }
    }

    fun delete(pk: Any): Boolean {
        var result = false
        val em = getEntityManager()
        try {
            val transaction = em.transaction
            try {
                transaction.begin()
                val findedObj = em.find(persistentClass, pk)
                em.remove(findedObj)
                transaction.commit()
                result = true
            } catch (e: Exception) {
                when (e) {
                    is IllegalArgumentException, is TransactionRequiredException -> {
                        LOGGER.error(e.message, e)
                        throw e
                    }
                    else -> throw e
                }
            } finally {
                if (transaction.isActive) {
                    transaction.rollback()
                }
            }
        } finally {
            em.close()
        }
        return result
    }

    open fun update(obj: T): Boolean {
        var result = false
        val em = getEntityManager()
        try {
            val transaction = em.transaction
            try {
                transaction.begin()
                em.merge(obj)
                transaction.commit()
                result = true
            } catch (e: Exception) {
                when (e) {
                    is EntityExistsException, is IllegalArgumentException, is TransactionRequiredException -> {
                        LOGGER.error(e.message, e)
                        throw e
                    }
                    else -> throw e
                }
            } finally {
                if (transaction.isActive) {
                    transaction.rollback()
                }
            }
        } finally {
            em.close()
        }
        return result
    }

}