package by.rekuts.giftcertificates.service;

import java.util.List;

public interface CrudService<T> {
    /**
     * This method inserts object of T class to database
     * @param t - object of the T class
     * @return relative path to the created object
     * @see by.rekuts.giftcertificates.service.dto.CertificateDto
     * @see by.rekuts.giftcertificates.service.dto.TagDto
     */
    String create(T t) throws ServiceException;

    /**
     * This method updates t-object in database
     * @param t - object
     */
    void update(T t) throws ServiceException;

    /**
     * This method deletes t with id <b>id</b> from database
     * @param id - id of the t-object
     */
    void delete(int id);

    /**
     * @return list of all t-objects from database
     */
    List<T> getList(Integer page, Integer itemsPerPage);
}
