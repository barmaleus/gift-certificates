package by.rekuts.giftcertificates.repository;

public enum QueryToDatabase {
    CREATE_TAG("INSERT INTO gift_tag (name) VALUES (?)"),
    UPDATE_TAG("UPDATE gift_tag SET name = ? WHERE tag_id = ?"),
    DELETE_TAG("DELETE FROM gift_tag WHERE tag_id = ?"),
    SELECT_ALL_TAGS("SELECT tag_id, name FROM gift_tag"),
    SELECT_ONE_TAG_BY_NAME("SELECT tag_id, name FROM gift_tag WHERE name = ?"),

    CREATE_CERT("INSERT INTO gift_certificate (name, description, price, expiration_days) VALUES (?, ?, ?, ?)"),
    UPDATE_CERT("UPDATE gift_certificate SET name = ?, description = ?, price = ?, expiration_days = ?, modification_date = ?) WHERE id = ?"),
    DELETE_CERT("DELETE FROM gift_certificate WHERE id = ?"),
    SELECT_ALL_CERTS("SELECT id, name, description, price, creation_date, modification_date, expiration_days FROM gift_certificate"),
    SELECT_CERTS_BY_TAG_NAME_DESCR("SELECT id, gift_certificate.name, description, price, creation_date, modification_date, expiration_days " +
            "FROM gift_certificate " +
            "INNER JOIN gift_tag ON gift_tag.tag_id = gift_certificate.id " +
            "WHERE gift_tag.name LIKE '%?%' AND (gift_certificate.name LIKE '%?%' OR description LIKE '%?%')"),
    SELECT_CERTS_BY_NAME_DESCR("SELECT id, gift_certificate.name, description, price, creation_date, modification_date, expiration_days " +
            "FROM gift_certificate " +
            "WHERE gift_certificate.name LIKE '%?%' OR description LIKE '%?%'"),
    SELECT_ONE_CERT_BY_ID("SELECT id, name, description, price, creation_date, modification_date, expiration_days FROM gift_certificate WHERE id = ?"),
    SELECT_CREATED_CERT_ID("SELECT currval('gift_certificate_id_seq')");

    private final String query;
    QueryToDatabase(String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }
}
