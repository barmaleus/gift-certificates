package by.rekuts.giftcertificates.repository.repos.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.List;

class EntitiesExtractor<T> {
    List<T> getListUsingCriteriaBuilder(EntityManager entityManager, List<Predicate> predicates, CriteriaQuery<T> criteriaQuery) {
        if (!predicates.isEmpty()) {
            criteriaQuery.where(
                    predicates.toArray(new Predicate[]{})
            );
        }
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
