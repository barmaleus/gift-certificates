package by.rekuts.giftcertificates.repository.repos;

import by.rekuts.giftcertificates.repository.specs.Specification;

import java.util.List;

public interface Repository<T> {
    /**
     * This method inserts object of T class to database
     * @param t - object of the T class
     * @return relative path to created element
     * @see by.rekuts.giftcertificates.repository.domain.Certificate
     * @see by.rekuts.giftcertificates.repository.domain.Tag
     */
    String create(T t);

    /**
     * This method updates t-object in database
     * @param t - object
     */
    void update(T t);

    /**
     * This method deletes t with id <b>id</b> from database
     * @param id - id of the t-object
     */
    void delete(int id);

    /**
     * @param specification - list of predicates for criteria builder
     * @param page - number of page, begins from 0
     * @param itemsPerPage - number of items on one page
     * @return list of all t-objects from database by any criteria, mentioned in specification
     */
    List<T> getList(Specification specification, Integer page, Integer itemsPerPage);
}
