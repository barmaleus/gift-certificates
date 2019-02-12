package by.rekuts.giftcertificates.service.converter;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.domain.User;
import by.rekuts.giftcertificates.repository.repos.UserRepository;
import by.rekuts.giftcertificates.repository.specs.UserSpecification;
import by.rekuts.giftcertificates.service.dto.CertificateDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CertificateConverter implements CommonConverter<Certificate, CertificateDto> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Certificate dtoConvert(CertificateDto certDto) {
        Certificate cert = new Certificate();
        cert.setId(certDto.getCertificateId());
        return initCertificateFields(certDto, cert);
    }

    @Override
    public CertificateDto domainConvert(Certificate cert) {
        CertificateDto certDto = new CertificateDto();
        certDto.setCertificateId(cert.getId());
        certDto.setName(cert.getName());
        certDto.setDescription(cert.getDescription());
        certDto.setPrice(cert.getPrice());
        certDto.setCreationDate(cert.getCreationDate());
        certDto.setModificationDate(cert.getModificationDate());
        certDto.setExpirationDays(cert.getExpirationDays());
        if (cert.getUsers() == null) {
            certDto.setUsers(null);
        } else {
            certDto.setUsers(cert.getUsers()
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toList()));
        }
        return certDto;
    }

    public Certificate dtoConvertForSaving(CertificateDto certDto) {
        Certificate cert = new Certificate();
        return initCertificateFields(certDto, cert);
    }

    private Certificate initCertificateFields(CertificateDto certDto, Certificate cert) {
        cert.setName(certDto.getName());
        cert.setDescription(certDto.getDescription());
        cert.setPrice(certDto.getPrice());
        cert.setCreationDate(certDto.getCreationDate());
        cert.setModificationDate(certDto.getModificationDate());
        cert.setExpirationDays(certDto.getExpirationDays());
        if (certDto.getUsers() == null) {
            cert.setUsers(null);
        } else {
            List<User> users = certDto.getUsers()
                    .stream()
                    .map(userId -> userRepository
                            .getList(new UserSpecification(userId), null, null)
                            .stream()
                            .findFirst().orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            cert.setUsers(users);
        }
        return cert;
    }


}
