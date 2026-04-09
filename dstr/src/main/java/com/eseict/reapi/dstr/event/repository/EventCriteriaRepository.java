package com.eseict.reapi.dstr.event.repository;

import com.eseict.reapi.dstr.event.domain.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class EventCriteriaRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Event> getEvents(List<String> eventIdList, String startDtm, String endDtm, int page, int rows) {
        // PostgreSQL JIT 비활성화 (복잡한 동적 쿼리 성능 최적화)
        em.createNativeQuery("SET LOCAL jit = off").executeUpdate();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        // 발생 상태만 조회
        predicates.add(cb.equal(root.get("procSt"), "1"));

        if (eventIdList != null && !eventIdList.isEmpty()) {
            predicates.add(root.get("statEvetId").in(eventIdList));
        }

        if (startDtm != null && !startDtm.isEmpty()) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("outbDtm"), startDtm));
        }

        if (endDtm != null && !endDtm.isEmpty()) {
            String endDtmFull = LocalDate.parse(endDtm, DateTimeFormatter.ofPattern("yyyyMMdd"))
                .atTime(LocalTime.MAX)
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            predicates.add(cb.lessThanOrEqualTo(root.get("outbDtm"), endDtmFull));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("outbDtm")));

        TypedQuery<Event> query = em.createQuery(cq);

        if (page > 0) {
            query.setFirstResult((page - 1) * rows);
            query.setMaxResults(rows);
        }

        return query.getResultList();
    }

    public long countEvents(List<String> eventIdList, String startDtm, String endDtm) {
        em.createNativeQuery("SET LOCAL jit = off").executeUpdate();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Event> root = cq.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("procSt"), "1"));

        if (eventIdList != null && !eventIdList.isEmpty()) {
            predicates.add(root.get("statEvetId").in(eventIdList));
        }

        if (startDtm != null && !startDtm.isEmpty()) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("outbDtm"), startDtm));
        }

        if (endDtm != null && !endDtm.isEmpty()) {
            String endDtmFull = LocalDate.parse(endDtm, DateTimeFormatter.ofPattern("yyyyMMdd"))
                .atTime(LocalTime.MAX)
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            predicates.add(cb.lessThanOrEqualTo(root.get("outbDtm"), endDtmFull));
        }

        cq.select(cb.count(root));
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult();
    }
}
