package by.rekuts.giftcertificates.service.impl;

import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.specs.CertificateSpecification;
import by.rekuts.giftcertificates.service.CertificateService;
import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.converter.CertificateConverter;
import by.rekuts.giftcertificates.service.dto.CertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository repository;
    private final TagRepository tagRepository;
    private final CertificateConverter converter;

    @Autowired
    public CertificateServiceImpl(CertificateRepository repository, TagRepository tagRepository, CertificateConverter converter) {
        this.repository = repository;
        this.tagRepository = tagRepository;
        this.converter = converter;
    }

    @Transactional
    @Override
    public String create(CertificateDto certDto) throws ServiceException {
        if (certDto.getName() != null && certDto.getPrice() != null) {
            return repository.create(converter.dtoConvertForSaving(certDto));
        } else {
            throw new ServiceException("Can't create new certificate. Name or price is expected.");
        }
    }

    @Transactional
    @Override
    public void update(CertificateDto certDto) throws ServiceException {
        if(certDto.getName() != null && certDto.getPrice() != null && certDto.getCertificateId() != 0) {
            repository.update(converter.dtoConvert(certDto));
        } else {
            throw new ServiceException("Can't update certificate. Name, price or id is expected.");
        }
    }

    @Transactional
    @Override
    public void delete(int id) {
        repository.delete(id);
    }

    @Override
    public List<CertificateDto> getList(Integer page, Integer itemsPerPage) {
        return repository
                .getList(new CertificateSpecification(), page, itemsPerPage)
                .stream()
                .map(converter::domainConvert)
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificateDto> getList(Map<String, String[]> params, Integer page, Integer itemsPerPage) {
        return repository
                .getList(new CertificateSpecification(tagRepository, params), page, itemsPerPage)
                .stream()
                .map(converter::domainConvert)
                .collect(Collectors.toList());
    }

    @Override
    public CertificateDto getCertById(int id) {
        return repository
                .getList(new CertificateSpecification(id), null, null)
                .stream()
                .map(converter::domainConvert)
                .findFirst()
                .orElse(null);
    }
}
