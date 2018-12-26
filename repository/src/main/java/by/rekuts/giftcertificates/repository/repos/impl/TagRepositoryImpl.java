package by.rekuts.giftcertificates.repository.repos.impl;

import by.rekuts.giftcertificates.repository.CustomConnectionPool;
import by.rekuts.giftcertificates.repository.DatabaseLabelNames;
import by.rekuts.giftcertificates.repository.QueryToDatabase;
import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.specs.SqlSpecification;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private static final Logger LOGGER = LogManager.getLogger(TagRepositoryImpl.class.getName());
    private static final String BASE_TAG_PATH = "/tag";

    @Autowired
    DataSource dataSource;
    @Autowired
    CustomConnectionPool connectionPool;

    private Connection connection = null;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public String create(Tag tag) {
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(QueryToDatabase.CREATE_TAG.getQuery());
            preparedStatement.setString(1, tag.getName());
            preparedStatement.executeUpdate();
            connectionPool.returnConnectionToThePool(connection);
            connection = null;
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Can't insert new tag to database. ", e);
        }
        return BASE_TAG_PATH + "/" + tag.getName();
    }

    @Override
    public void update(Tag tag) {
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(QueryToDatabase.UPDATE_TAG.getQuery());
            preparedStatement.setString(1, tag.getName());
            preparedStatement.setInt(2, tag.getTagId());
            preparedStatement.executeUpdate();
            connectionPool.returnConnectionToThePool(connection);
            connection = null;
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Can't update tag in database. Id: " + tag.getTagId(), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(QueryToDatabase.DELETE_TAG.getQuery());
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            connectionPool.returnConnectionToThePool(connection);
            connection = null;
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Can't delete tag from database. Tag id: " + id, e);
        }
    }

    @Override
    public List<Tag> getList(SqlSpecification sqlSpecification) {

        List<Tag> resultList = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sqlSpecification.getSqlQuery());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Tag tag = new Tag();
                int tagId = resultSet.getInt(DatabaseLabelNames.COLUMN_LABEL_TAG_ID.getName());
                String tagName = resultSet.getString(DatabaseLabelNames.COLUMN_LABEL_TAG_NAME.getName());
                tag.setTagId(tagId);
                tag.setName(tagName);
                resultList.add(tag);
            }
            connectionPool.returnConnectionToThePool(connection);
            connection = null;
        } catch (SQLException e) {
            LOGGER.log(Level.WARN, "Can't select tags from database. " + e);
        }
        return resultList;
    }
}
