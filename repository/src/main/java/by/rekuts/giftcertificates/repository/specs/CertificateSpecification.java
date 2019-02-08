package by.rekuts.giftcertificates.repository.specs;

import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.repository.repos.TagRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CertificateSpecification extends Specification {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private Integer expirationDays;
    private Integer tagId;
    private String searchKey;

    public CertificateSpecification() {}

    public CertificateSpecification(Integer id) {
        this.id = id;
    }

    public CertificateSpecification(Map<String, String> params, TagRepository repository) {
        if(params.containsKey("tag")){
            Tag tag = repository.getList(new TagSpecification(params.get("tag"))).get(0);
            tagId = tag.getId();
        }
        searchKey = params.get("search");
    }

    @Override
    public List<Predicate> getPredicates(Root root, CriteriaBuilder builder) {
        List<Predicate> predicates = preparePredicates(root, builder, id, name);
        if (description != null) {
            predicates.add(
                    builder.equal(root.get("description"), description)
            );
        }
        if (price != null) {
            predicates.add(
                    builder.equal(root.get("price"), price)
            );
        }
        if (creationDate != null) {
            predicates.add(
                    builder.equal(root.get("creation_date"), creationDate)
            );
        }
        if (modificationDate != null) {
            predicates.add(
                    builder.equal(root.get("modification_date"), modificationDate)
            );
        }
        if (expirationDays != null) {
            predicates.add(
                    builder.equal(root.get("expiration_days"), expirationDays)
            );
        }
        if (tagId != null) {
            predicates.add(
                    builder.equal(root.join("tags"), tagId)
            );
        }
        if (searchKey != null) {
            predicates.add(
                    builder.or(
                            builder.like(root.get("name").as(String.class), "%" + searchKey + "%"),
                            builder.like(root.get("description").as(String.class), "%" + searchKey + "%")
                    )
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getExpirationDays() {
        return expirationDays;
    }

    public void setExpirationDays(Integer expirationDays) {
        this.expirationDays = expirationDays;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
