package by.rekuts.giftcertificates.service.dto;

import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseDto extends ResourceSupport {
    private int userId;
    private int certId;
    private BigDecimal price;
    private LocalDateTime purchaseTime;

    public PurchaseDto(int userId, int certId, BigDecimal price, LocalDateTime purchaseTime) {
        this.userId = userId;
        this.certId = certId;
        this.price = price;
        this.purchaseTime = purchaseTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCertId() {
        return certId;
    }

    public void setCertId(int certId) {
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

    @Override
    public String toString() {
        return "PurchaseDto{" +
                "userId=" + userId +
                ", certId=" + certId +
                ", price=" + price +
                ", purchaseTime=" + purchaseTime +
                '}';
    }
}
