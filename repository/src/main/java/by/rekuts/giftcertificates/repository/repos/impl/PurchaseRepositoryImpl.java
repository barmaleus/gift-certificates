package by.rekuts.giftcertificates.repository.repos.impl;

import by.rekuts.giftcertificates.repository.domain.Purchase;
import by.rekuts.giftcertificates.repository.repos.PurchaseRepository;
import by.rekuts.giftcertificates.repository.specs.PurchaseSpecification;
import by.rekuts.giftcertificates.repository.specs.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PurchaseRepositoryImpl implements PurchaseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String create(Purchase purchase) {
        entityManager.persist(purchase);
        entityManager.flush();
        return "/users" + purchase.getUser().getId() + "/certificate" + purchase.getCertificate().getId() + "/purchase";
    }

    @Override
    public void update(Purchase purchase) {
        // do nothing
    }

    @Override
    public void delete(int id) {
        // do nothing
    }

    @Override
    public List<Purchase> getList(Specification specification, Integer page, Integer itemsPerPage) {

        final PurchaseSpecification purchaseSpecification = (PurchaseSpecification) specification;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Purchase> criteriaQuery = builder.createQuery(Purchase.class);
        Root<Purchase> purchaseRoot = criteriaQuery.from(Purchase.class);
        List<Predicate> predicates;
        predicates = purchaseSpecification.getPredicates(purchaseRoot, builder);

        return new EntitiesExtractor<Purchase>().getListUsingCriteriaBuilder(entityManager, predicates, criteriaQuery, page, itemsPerPage);
    }
}
