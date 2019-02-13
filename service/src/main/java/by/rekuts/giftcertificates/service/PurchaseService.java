package by.rekuts.giftcertificates.service;

import by.rekuts.giftcertificates.service.dto.PurchaseDto;

public interface PurchaseService extends CrudService<PurchaseDto>{

    PurchaseDto getList(Integer userId, Integer certId, Integer page, Integer itemsPerPage);
    boolean buyCertificate(String username, int certId) throws ServiceException;
}
