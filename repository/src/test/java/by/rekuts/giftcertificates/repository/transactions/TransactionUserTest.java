package by.rekuts.giftcertificates.repository.transactions;

import by.rekuts.giftcertificates.repository.TestConfig;
import by.rekuts.giftcertificates.repository.domain.User;
import by.rekuts.giftcertificates.repository.repos.UserRepository;
import by.rekuts.giftcertificates.repository.specs.UserSpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("debug")
@Transactional
public class TransactionUserTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRepository repository;

    private int count;
    private User userBeforeTransaction;

    @BeforeTransaction
    void verifyInitialDatabaseState() {
        count = countRowsInTable(new User());
        userBeforeTransaction = repository.getList(new UserSpecification(2)).get(0);
    }

    @Test
    public void createUserRollback() {
        User user = new User();
        user.setLogin("supersu");
        user.setPassword(user.getLogin());

        repository.create(user);
        assertNumUsers(count + 1);
        TestTransaction.end();
        assertNumUsers(count);
    }

    @Test
    public void createUserCommit() {
        User user = new User();
        user.setLogin("superuser");
        user.setPassword(user.getLogin());
        repository.create(user);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        assertNumUsers(count + 1);
    }

    @Test
    public void updateUserRollback() {
        User userForUpdate = new User();
        userForUpdate.setId(2);
        userForUpdate.setLogin("superuser-user");
        userForUpdate.setPassword(userForUpdate.getLogin());

        repository.update(userForUpdate);
        User userAfterUpdate = repository.getList(new UserSpecification(2)).get(0);
        assertNotEquals(userBeforeTransaction.getLogin(), userAfterUpdate.getLogin());
        TestTransaction.end();
        User userAfterTransaction = repository.getList(new UserSpecification(2)).get(0);
        assertEquals(userBeforeTransaction.getLogin(), userAfterTransaction.getLogin());
    }

    @Test
    public void updateUserCommit() {
        User userForUpdate = new User();
        userForUpdate.setId(2);
        userForUpdate.setLogin("su");
        userForUpdate.setPassword(userForUpdate.getLogin());

        repository.update(userForUpdate);
        User userAfterUpdate = repository.getList(new UserSpecification(2)).get(0);
        assertNotEquals(userBeforeTransaction.getLogin(), userAfterUpdate.getLogin());
        TestTransaction.flagForCommit();
        TestTransaction.end();
        User userAfterTransaction = repository.getList(new UserSpecification(2)).get(0);
        assertNotEquals(userBeforeTransaction.getLogin(), userAfterTransaction.getLogin());
    }

    @Test
    public void deleteCertificateRollback() {
        repository.delete(2);
        assertNumUsers(count - 1);
        TestTransaction.end();
        assertNumUsers(count);
    }

    @Test
    public void deleteCertificateCommit() {
        repository.delete(1);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        assertNumUsers(count - 1);
    }

    protected int countRowsInTable(Object object) {
        Query query = entityManager.createQuery("SELECT count(t) FROM " + object.getClass().getSimpleName() + " t");
        return (int) (long) query.getSingleResult();
    }

    protected void assertNumUsers(int expected) {
        assertEquals("Number of rows in the gift_user table.", expected, countRowsInTable(new User()));
    }
}
