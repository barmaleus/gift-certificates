package by.rekuts.giftcertificates.service;

import by.rekuts.giftcertificates.service.dto.CertificateDto;

import java.util.List;
import java.util.Map;

public interface CertificateService extends CrudService<CertificateDto> {

    /**
     * @return list of all certificateDto objects from database
     */
    List<CertificateDto> getList(Map<String, String> params, Integer page, Integer itemsPerPage);

    /**
     * @return certificateDto object from database reached by id
     */
    CertificateDto getCertById(int id);
}
