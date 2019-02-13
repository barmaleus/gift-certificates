package by.rekuts.giftcertificates.service.converter;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.domain.Purchase;
import by.rekuts.giftcertificates.repository.domain.User;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.repos.PurchaseRepository;
import by.rekuts.giftcertificates.repository.repos.UserRepository;
import by.rekuts.giftcertificates.repository.specs.CertificateSpecification;
import by.rekuts.giftcertificates.repository.specs.UserSpecification;
import by.rekuts.giftcertificates.service.dto.PurchaseDto;
import org.springframework.beans.factory.annotation.Autowired;

public class PurchaseConverter implements CommonConverter<Purchase, PurchaseDto> {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public Purchase dtoConvert(PurchaseDto dto) {
        User user = userRepository
                .getList(new UserSpecification(dto.getUserId()), null, null)
                .stream()
                .findFirst().orElse(null);
        Certificate certificate = certificateRepository
                .getList(new CertificateSpecification(dto.getCertId()), null, null)
                .stream()
                .findFirst().orElse(null);

        return new Purchase(user, certificate, dto.getPrice(), dto.getPurchaseTime());
    }

    @Override
    public PurchaseDto domainConvert(Purchase purchase) {
        return new PurchaseDto(
                purchase.getUser().getId(),
                purchase.getCertificate().getId(),
                purchase.getPrice(),
                purchase.getPurchaseTime());
    }
}
