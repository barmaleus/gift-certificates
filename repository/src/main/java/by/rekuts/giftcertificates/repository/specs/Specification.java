package by.rekuts.giftcertificates.repository.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public abstract class Specification {
    abstract List<Predicate> getPredicates(Root root, CriteriaBuilder builder);

    List<Predicate> preparePredicates(Root root, CriteriaBuilder builder, Integer id, String name) {
        List<Predicate> predicates = new ArrayList<>();
        if (id != null) {
            predicates.add(
                    builder.equal(root.get("id"), id)
            );
        }
        if (name != null) {
            predicates.add(
                    builder.equal(root.get("name"), name)
            );
        }
        return predicates;
    }
}
