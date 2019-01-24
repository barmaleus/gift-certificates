package by.rekuts.giftcertificates.service.converter;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.service.dto.CertificateDto;

public class CertificateConverter implements CommonConverter<Certificate, CertificateDto> {
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
        return cert;
    }


}
