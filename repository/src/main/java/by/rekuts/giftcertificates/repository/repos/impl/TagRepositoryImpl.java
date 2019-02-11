package by.rekuts.giftcertificates.repository.repos.impl;

import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.specs.Specification;
import by.rekuts.giftcertificates.repository.specs.TagSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private static final String BASE_TAG_PATH = "/tags";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String create(Tag tag) {
        entityManager.persist(tag);
        entityManager.flush();
        return BASE_TAG_PATH + "/" + tag.getName();
    }

    @Override
    public void update(Tag tag) {
        //do nothing
        //delete the old tag and create a new one
    }

    @Override
    public void delete(int id) {
       entityManager.remove(entityManager.find(Tag.class, id));
    }

    @Override
    public List<Tag> getList(Specification specification, Integer page, Integer itemsPerPage) {

        final TagSpecification tagSpecification = (TagSpecification) specification;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = builder.createQuery(Tag.class);
        Root<Tag> certificateRoot = criteriaQuery.from(Tag.class);
        List<Predicate> predicates;
        predicates = tagSpecification.getPredicates(certificateRoot, builder);

        return new EntitiesExtractor<Tag>().getListUsingCriteriaBuilder(entityManager, predicates, criteriaQuery, page, itemsPerPage);
    }
}
