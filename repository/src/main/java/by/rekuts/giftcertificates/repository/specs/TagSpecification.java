package by.rekuts.giftcertificates.repository.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class TagSpecification extends Specification {
    private Integer id;
    private String name;
    private Integer certificateId;

    public TagSpecification() {}

    public TagSpecification(String name) {
        this.name = name;
    }

    @Override
    public List<Predicate> getPredicates(Root root, CriteriaBuilder builder) {
        List<Predicate> predicates = preparePredicates(root, builder, id, name);
        if (certificateId != null) {
            predicates.add(
                    builder.equal(root.join("tags"), certificateId)
            );
        }
        return predicates;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Integer certificateId) {
        this.certificateId = certificateId;
    }
}
