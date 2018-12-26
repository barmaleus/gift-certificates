package by.rekuts.giftcertificates.repository.repos.impl;

import by.rekuts.giftcertificates.repository.CustomConnectionPool;
import by.rekuts.giftcertificates.repository.DatabaseLabelNames;
import by.rekuts.giftcertificates.repository.QueryToDatabase;
import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.specs.SqlSpecification;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {
    private static final Logger LOGGER = LogManager.getLogger(TagRepositoryImpl.class.getName());
    private static final String BASE_CERTIFICATE_PATH = "/certificate";

    @Autowired
    DataSource dataSource;
    @Autowired
    CustomConnectionPool connectionPool;

    private Connection connection = null;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public String create(Certificate certificate) {
        int createdCertificateId = -1;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(QueryToDatabase.CREATE_CERT.getQuery());
            preparedStatement.setString(1, certificate.getName());
            preparedStatement.setString(2, certificate.getDescription());
            preparedStatement.setBigDecimal(3, certificate.getPrice());
            preparedStatement.setInt(4, certificate.getExpirationDays());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(QueryToDatabase.SELECT_CREATED_CERT_ID.getQuery());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                createdCertificateId = resultSet.getInt(1);
            }
            connectionPool.returnConnectionToThePool(connection);
            connection = null;
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Can't insert new certificate to database. ", e);
        }
        return BASE_CERTIFICATE_PATH + "/" + createdCertificateId;
}

    @Override
    public void update(Certificate certificate) {
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(QueryToDatabase.UPDATE_CERT.getQuery());
            preparedStatement.setString(1, certificate.getName());
            preparedStatement.setString(2, certificate.getDescription());
            preparedStatement.setBigDecimal(3, certificate.getPrice());
            preparedStatement.setInt(4, certificate.getExpirationDays());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(6, certificate.getCertificateId());
            preparedStatement.executeUpdate();
            connectionPool.returnConnectionToThePool(connection);
            connection = null;
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Can't update certificate in database. Id: " + certificate.getCertificateId(), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(QueryToDatabase.DELETE_CERT.getQuery());
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            connectionPool.returnConnectionToThePool(connection);
            connection = null;
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Can't delete certificate from database. Certificate id: " + id, e);
        }
    }

    @Override
    public List<Certificate> getList(SqlSpecification sqlSpecification) {

        List<Certificate> resultList = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sqlSpecification.getSqlQuery());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Certificate certificate = new Certificate();
                int certId = resultSet.getInt(DatabaseLabelNames.COLUMN_LABEL_CERT_ID.getName());
                String certName = resultSet.getString(DatabaseLabelNames.COLUMN_LABEL_CERT_NAME.getName());
                String descr = resultSet.getString(DatabaseLabelNames.COLUMN_LABEL_CERT_DESCR.getName());
                BigDecimal price = resultSet.getBigDecimal(DatabaseLabelNames.COLUMN_LABEL_CERT_PRICE.getName());
                LocalDateTime creation = resultSet.getTimestamp(DatabaseLabelNames.COLUMN_LABEL_CERT_CREATION.getName()).toLocalDateTime();
                Timestamp timestampModification = resultSet.getTimestamp(DatabaseLabelNames.COLUMN_LABEL_CERT_MODIFICATION.getName());
                LocalDateTime modification = null;
                if (timestampModification != null) {
                    modification = timestampModification.toLocalDateTime();
                }
                int expiration = resultSet.getInt(DatabaseLabelNames.COLUMN_LABEL_CERT_EXPIRATION.getName());
                certificate.setCertificateId(certId);
                certificate.setName(certName);
                certificate.setDescription(descr);
                certificate.setPrice(price);
                certificate.setCreationDate(creation);
                certificate.setModificationDate(modification);
                certificate.setExpirationDays(expiration);
                resultList.add(certificate);
            }
            connectionPool.returnConnectionToThePool(connection);
            connection = null;
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Can't select certificates from database. " + e);
        }
        return resultList;
    }
}
