package by.rekuts.giftcertificates.service.impl;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.domain.Purchase;
import by.rekuts.giftcertificates.repository.domain.User;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.repos.PurchaseRepository;
import by.rekuts.giftcertificates.repository.repos.UserRepository;
import by.rekuts.giftcertificates.repository.specs.CertificateSpecification;
import by.rekuts.giftcertificates.repository.specs.PurchaseSpecification;
import by.rekuts.giftcertificates.repository.specs.UserSpecification;
import by.rekuts.giftcertificates.service.PurchaseService;
import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.converter.PurchaseConverter;
import by.rekuts.giftcertificates.service.dto.PurchaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final PurchaseConverter converter;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, UserRepository userRepository, CertificateRepository certificateRepository, PurchaseConverter converter) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
        this.converter = converter;
    }

    @Override
    public String create(PurchaseDto purchaseDto) throws ServiceException {
        return null;
    }

    @Override
    public void update(PurchaseDto purchaseDto) throws ServiceException {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<PurchaseDto> getList(Integer page, Integer itemsPerPage) {
        return purchaseRepository
                .getList(new PurchaseSpecification(), page, itemsPerPage)
                .stream()
                .map(converter::domainConvert)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseDto getList(Integer userId, Integer certId, Integer page, Integer itemsPerPage) {
        return purchaseRepository
                .getList(new PurchaseSpecification(userId, certId), page, itemsPerPage)
                .stream()
                .map(converter::domainConvert)
                .findFirst().orElse(null);
    }

    @Transactional
    @Override
    public boolean buyCertificate(String username, int certId) throws ServiceException {
        User user = userRepository
                .getList(new UserSpecification(username), null, null)
                .stream()
                .findFirst().orElseThrow(() -> new ServiceException("Sorry, you can't buy certificate now. "));
        Certificate certificate = certificateRepository
                .getList(new CertificateSpecification(certId), null, null)
                .stream()
                .findFirst().orElseThrow(() -> new ServiceException("Sorry, you can't buy certificate now. "));
        Purchase purchase = purchaseRepository
                .getList(new PurchaseSpecification(user.getId(), certId), null, null)
                .stream()
                .findFirst().orElse(null);
        if (purchase != null) {
            return false;
        } else {
            purchase = new Purchase(user, certificate, certificate.getPrice());
            purchaseRepository.create(purchase);
            return true;
        }
    }
}
