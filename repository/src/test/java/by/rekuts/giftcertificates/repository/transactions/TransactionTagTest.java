package by.rekuts.giftcertificates.repository.transactions;

import by.rekuts.giftcertificates.repository.TestConfig;
import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.specs.TagSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("debug")
@DirtiesContext
@Transactional
public class TransactionTagTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TagRepository repository;

    private int count;

    @BeforeTransaction
    void verifyInitialDatabaseState() {
        count = countRowsInTable(new Tag());
    }

    @Test
    public void createTagRollback() {
        Tag tag = new Tag();
        tag.setName("abc");

        repository.create(tag);
        assertNumTags(count + 1);
        TestTransaction.isFlaggedForRollback();
        TestTransaction.end();
        assertNumTags(count);
    }

    @Test
    public void deleteTagRollback() {
        List<Tag> tags = repository.getList(new TagSpecification());
        repository.delete(tags.get(tags.size()-1).getId());
        assertNumTags(count - 1);
        TestTransaction.isFlaggedForRollback();
    }

    @Test
    public void createTagCommit() {
        Tag tag = new Tag();
        tag.setName("abc");
        repository.create(tag);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        assertNumTags(count + 1);
    }

    @Test
    public void deleteTagCommit() {
        List<Tag> tags = repository.getList(new TagSpecification());
        repository.delete(tags.get(tags.size()-1).getId());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        assertNumTags(count - 1);
    }

    @Test
    public void deleteTagRollback1() {
        List<Tag> tags = repository.getList(new TagSpecification());
        repository.delete(tags.get(tags.size()-1).getId());

        TestTransaction.flagForRollback();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        assertNumTags(count);
    }

    protected int countRowsInTable(Object object) {
        Query query = entityManager.createQuery("SELECT count(t) FROM " + object.getClass().getSimpleName() + " t");
        return (int) (long) query.getSingleResult();
    }

    protected void assertNumTags(int expected) {
        assertEquals("Number of rows in the curreent table.", expected, countRowsInTable(new Tag()));
    }
}