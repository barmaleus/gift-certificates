package by.rekuts.giftcertificates.repository.repos.impl;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.domain.Purchase;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.specs.CertificateSpecification;
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
public class  CertificateRepositoryImpl implements CertificateRepository {
    private static final String BASE_CERTIFICATE_PATH = "/certificates";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String create(Certificate certificate) {
        if(certificate.getExpirationDays() == null) {
            certificate.setExpirationDays(1);
        }
        entityManager.persist(certificate);
        entityManager.flush();
        return BASE_CERTIFICATE_PATH + "/" + certificate.getId();
}

    @Override
    public void update(Certificate certificate) {
        Certificate dbCertificate = entityManager.find(Certificate.class, certificate.getId());
        dbCertificate.setName(certificate.getName());
        dbCertificate.setDescription(certificate.getDescription());
        dbCertificate.setPrice(certificate.getPrice());
        dbCertificate.setExpirationDays(certificate.getExpirationDays());
        dbCertificate.setUsers(certificate.getUsers());
        entityManager.merge(dbCertificate);
        entityManager.flush();
    }

    @Override
    public void delete(int id) {
        entityManager.remove(entityManager.find(Certificate.class, id));
    }

    @Override
    public List<Certificate> getList(Specification specification, Integer page, Integer itemsPerPage) {

        final CertificateSpecification certificateSpecification = (CertificateSpecification) specification;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = builder.createQuery(Certificate.class);
        Root<Certificate> certificateRoot = criteriaQuery.from(Certificate.class);
        List<Predicate> predicates;
        predicates = certificateSpecification.getPredicates(certificateRoot, builder);

        return new EntitiesExtractor<Certificate>().getListUsingCriteriaBuilder(entityManager, predicates, criteriaQuery, page, itemsPerPage);
    }
}
