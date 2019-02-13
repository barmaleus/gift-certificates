package by.rekuts.giftcertificates.repository.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PurchaseSpecification extends Specification {
    private Integer userId;
    private Integer certId;
    private BigDecimal price;
    private LocalDateTime purchaseTime;

    public PurchaseSpecification() {}

    public PurchaseSpecification(Integer userId, Integer certId) {
        this.userId = userId;
        this.certId = certId;
    }

    @Override
    public List<Predicate> getPredicates(Root root, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if (userId != null) {
            predicates.add(
                    builder.equal(root.get("user"), userId)
            );
        }
        if (certId != null) {
            predicates.add(
                    builder.equal(root.get("certificate"), certId)
            );
        }
        if (price != null) {
            predicates.add(
                    builder.equal(root.get("price"), price)
            );
        }
        if (purchaseTime != null) {
            predicates.add(
                    builder.equal(root.get("purchase_time"), purchaseTime)
            );
        }
        return predicates;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCertId() {
        return certId;
    }

    public void setCertId(Integer certId) {
        this.certId = certId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
}
