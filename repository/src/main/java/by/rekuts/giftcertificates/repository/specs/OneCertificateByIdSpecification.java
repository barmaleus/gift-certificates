package by.rekuts.giftcertificates.repository.specs;

import by.rekuts.giftcertificates.repository.QueryToDatabase;

public class OneCertificateByIdSpecification implements SqlSpecification {
    public OneCertificateByIdSpecification (String id) {
        this.id = id;
    }

    private final String id;

    @Override
    public String getSqlQuery() {
        String query = QueryToDatabase.SELECT_ONE_CERT_BY_ID.getQuery();
        query = query.replaceFirst("\\?", "'" + id + "'");
        return query;
    }
}
