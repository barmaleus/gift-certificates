package by.rekuts.giftcertificates.repository.specs;

import by.rekuts.giftcertificates.repository.QueryToDatabase;

public class AllTagsSpecification implements SqlSpecification {
    @Override
    public String getSqlQuery() {
        return QueryToDatabase.SELECT_ALL_TAGS.getQuery();
    }
}