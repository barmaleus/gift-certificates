package by.rekuts.giftcertificates.repository.repos.impl;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.specs.CertificateSpecification;
import by.rekuts.giftcertificates.repository.specs.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {
    private static final String BASE_CERTIFICATE_PATH = "/certificate";

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public String create(Certificate certificate) {
        certificate.setCreationDate(LocalDateTime.now());
        if(certificate.getExpirationDays() == null) {
            certificate.setExpirationDays(1);
        }
        entityManager.persist(certificate);
        entityManager.flush();
        return BASE_CERTIFICATE_PATH + "/" + certificate.getId();
}

    @Override
    public void update(Certificate certificate) {
        if(certificate.getName() == null || certificate.getPrice() == null) {
            throw new PersistenceException("Certificate update error. Name or price is expected");
        }
        Certificate dbCertificate = entityManager.find(Certificate.class, certificate.getId());
        dbCertificate.setName(certificate.getName());
        dbCertificate.setDescription(certificate.getDescription());
        dbCertificate.setPrice(certificate.getPrice());
        dbCertificate.setModificationDate(LocalDateTime.now());
        dbCertificate.setExpirationDays(certificate.getExpirationDays());
        entityManager.merge(dbCertificate);
    }

    @Override
    public void delete(int id) {
        entityManager.remove(entityManager.find(Certificate.class, id));
    }

    @Override
    public List<Certificate> getList(Specification specification) {

        final CertificateSpecification certificateSpecification = (CertificateSpecification) specification;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = builder.createQuery(Certificate.class);
        Root<Certificate> certificateRoot = criteriaQuery.from(Certificate.class);
        List<Predicate> predicates;
        predicates = certificateSpecification.getPredicates(certificateRoot, builder);

        if (!predicates.isEmpty()) {
            criteriaQuery.where(
                    predicates.toArray(new Predicate[]{})
            );
        }
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
