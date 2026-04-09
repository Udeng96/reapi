package com.eseict.reapi.dstr.facility.repository;

import com.eseict.reapi.common.domain.fms.ErfFacInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class FacilityCriteriaRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 시설물 목록 조회 (Criteria API)
     * type: "cctv" 면 CCTV만, "fac" 이면 CCTV 제외
     */
    public List<ErfFacInfo> getFacList(String type, String facNm, int page, int rows, String facClfyNm) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ErfFacInfo> cq = cb.createQuery(ErfFacInfo.class);
        Root<ErfFacInfo> root = cq.from(ErfFacInfo.class);
        root.fetch("refErfFacClfy", JoinType.LEFT);
        root.fetch("refErfPosCrdnts", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(cb, root, type, facNm, facClfyNm);
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("facNm")));
        cq.distinct(true);

        TypedQuery<ErfFacInfo> query = em.createQuery(cq);
        if (page > 0) {
            query.setFirstResult((page - 1) * rows);
            query.setMaxResults(rows);
        }
        return query.getResultList();
    }

    public long countFacList(String type, String facNm, String facClfyNm) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ErfFacInfo> root = cq.from(ErfFacInfo.class);

        List<Predicate> predicates = buildPredicates(cb, root, type, facNm, facClfyNm);
        cq.select(cb.count(root));
        cq.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getSingleResult();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<ErfFacInfo> root,
                                             String type, String facNm, String facClfyNm) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("useYn"), 1));

        if (facNm != null && !facNm.isBlank()) {
            predicates.add(cb.like(root.get("facNm"), "%" + facNm + "%"));
        }

        if (facClfyNm != null && !facClfyNm.isBlank()) {
            Join<?, ?> clfyJoin = root.join("refErfFacClfy", JoinType.LEFT);
            if ("cctv".equalsIgnoreCase(type)) {
                predicates.add(cb.like(cb.lower(clfyJoin.get("facClfyNm")), "%cctv%"));
            } else {
                predicates.add(cb.notLike(cb.lower(clfyJoin.get("facClfyNm")), "%cctv%"));
            }
        }

        return predicates;
    }
}
