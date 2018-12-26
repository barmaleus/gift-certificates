package by.rekuts.giftcertificates.repository.specs;

import by.rekuts.giftcertificates.repository.QueryToDatabase;

import java.util.Map;

public class SearchCertsSpecification implements SqlSpecification{
    public SearchCertsSpecification(Map<String, String> params) {
        this.params = params;
    }

    private final Map<String, String> params;

    @Override
    public String getSqlQuery() {
        String query;
        if(params.containsKey("tag")) {
            query = QueryToDatabase.SELECT_CERTS_BY_TAG_NAME_DESCR.getQuery();
            query = query.replaceFirst("\\?", params.get("tag"));
        } else {
            query = QueryToDatabase.SELECT_CERTS_BY_NAME_DESCR.getQuery();
        }
        if(params.containsKey("search")) {
            query = query.replaceAll("\\?", params.get("search"));
        } else {
            query = query.replaceAll("\\?", "");
        }
        return query;
    }
}
