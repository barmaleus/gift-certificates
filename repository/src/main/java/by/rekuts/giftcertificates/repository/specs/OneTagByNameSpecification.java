package by.rekuts.giftcertificates.repository.specs;

import by.rekuts.giftcertificates.repository.QueryToDatabase;

public class OneTagByNameSpecification implements SqlSpecification {

    public OneTagByNameSpecification(String name) {
        this.name = name;
    }

    private final String name;

    @Override
    public String getSqlQuery() {
        String query = QueryToDatabase.SELECT_ONE_TAG_BY_NAME.getQuery();
        query = query.replaceFirst("\\?", "'" + name + "'");
        return query;
    }
}
