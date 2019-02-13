package by.rekuts.giftcertificates.repository.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_certificate")
public class Purchase implements Serializable {

    @Id @ManyToOne @JoinColumn
    private User user;

    @Id @ManyToOne @JoinColumn
    private Certificate certificate;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, name = "purchase_time")
    private LocalDateTime purchaseTime;

    public Purchase() {}

    public Purchase(User user, Certificate certificate, BigDecimal price) {
        this.user = user;
        this.certificate = certificate;
        this.price = price;
    }

    public Purchase(User user, Certificate certificate, BigDecimal price, LocalDateTime purchaseTime) {
        this.user = user;
        this.certificate = certificate;
        this.price = price;
        this.purchaseTime = purchaseTime;
    }

    @PrePersist
    public void onPrePersist() {
        setPurchaseTime(LocalDateTime.now());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase that = (Purchase) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(certificate, that.certificate) &&
                Objects.equals(price, that.price) &&
                Objects.equals(purchaseTime, that.purchaseTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, certificate, price, purchaseTime);
    }
}
