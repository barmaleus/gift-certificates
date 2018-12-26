package by.rekuts.giftcertificates.repository;

public enum DatabaseLabelNames {
    COLUMN_LABEL_TAG_ID("tag_id"),
    COLUMN_LABEL_TAG_NAME("name"),
    TABLE_LABEL_TAG("gift_tag"),

    COLUMN_LABEL_CERT_ID("id"),
    COLUMN_LABEL_CERT_NAME("name"),
    COLUMN_LABEL_CERT_DESCR("description"),
    COLUMN_LABEL_CERT_PRICE("price"),
    COLUMN_LABEL_CERT_CREATION("creation_date"),
    COLUMN_LABEL_CERT_MODIFICATION("modification_date"),
    COLUMN_LABEL_CERT_EXPIRATION("expiration_days"),
    TABLE_LABEL_CERT("gift_certificate");

    private final String labelName;
    DatabaseLabelNames(String labelName) {
        this.labelName = labelName;
    }

    public String getName() {
        return this.labelName;
    }
}
