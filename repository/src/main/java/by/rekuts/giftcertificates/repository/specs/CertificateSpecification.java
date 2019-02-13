package by.rekuts.giftcertificates.repository.specs;

import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.repository.repos.TagRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<Integer> tagsId = new ArrayList<>();
    private String searchKey;
    private Integer userId;

    public CertificateSpecification() {}

    public CertificateSpecification(Integer id) {
        this.id = id;
    }

    public CertificateSpecification(TagRepository repository, Map<String, String[]> params) {
        if(params.containsKey("tag")){
            String[] tagNameArray = params.get("tag");
            for (String aTagNameArray : tagNameArray) {
                Integer tagId = repository
                        .getList(new TagSpecification(aTagNameArray), null, null)
                        .stream()
                        .map(Tag::getId)
                        .findFirst()
                        .orElse(-1);
                tagsId.add(tagId);
            }
        }
        if (params.get("search") != null) {
            searchKey = params.get("search")[0];
        }
        if (params.containsKey("userId")) {
            String userStringId = params.get("userId")[0];
            userId = Integer.parseInt(userStringId);
        }
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
        if (!tagsId.isEmpty()) {
            tagsId.forEach(
                    tagId -> predicates.add(
                            builder.equal(root.join("tags"), tagId)
            ));
        }
        if (searchKey != null) {
            predicates.add(
                    builder.or(
                            builder.like(root.get("name").as(String.class), "%" + searchKey + "%"),
                            builder.like(root.get("description").as(String.class), "%" + searchKey + "%")
                    )
            );
        }
        if (userId != null) {
            predicates.add(
                    builder.equal(root.join("users"), userId)
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

    public List<Integer> getTagsId() {
        return tagsId;
    }

    public void setTagsId(List<Integer> tagsId) {
        this.tagsId = tagsId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
