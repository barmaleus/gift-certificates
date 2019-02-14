package by.rekuts.giftcertificates.repository.transactions;

import by.rekuts.giftcertificates.repository.TestConfig;
import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.specs.CertificateSpecification;
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
import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("debug")
@DirtiesContext
@Transactional
public class TransactionCertificateTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
   private CertificateRepository repository;

    private int count;
    private Certificate certBeforeTransaction;

    @BeforeTransaction
    void verifyInitialDatabaseState() {
        count = countRowsInTable(new Certificate());
        certBeforeTransaction = repository.getList(new CertificateSpecification(71), null, null).get(0);
    }

    @Test
    public void createCertificateRollback() {
        Certificate certificate = new Certificate();
        certificate.setName("abc");
        certificate.setPrice(BigDecimal.TEN);

        repository.create(certificate);
        assertNumCerts(count + 1);
        TestTransaction.end();
        assertNumCerts(count);
    }

    @Test
    public void createCertificateCommit() {
        Certificate certificate = new Certificate();
        certificate.setName("abc");
        certificate.setPrice(BigDecimal.TEN);
        repository.create(certificate);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        assertNumCerts(count + 1);
    }

    @Test
    public void updateCertificateRollback() {
        Certificate certForUpdate = new Certificate();
        certForUpdate.setId(71);
        certForUpdate.setName("abc1");
        certForUpdate.setPrice(BigDecimal.valueOf(18));
        certForUpdate.setExpirationDays(56);

        repository.update(certForUpdate);
        Certificate certAfterUpdate = repository.getList(new CertificateSpecification(71), null, null).get(0);
        assertNotEquals(certBeforeTransaction.getName(), certAfterUpdate.getName());
        assertNotEquals(certBeforeTransaction.getModificationDate(), certAfterUpdate.getModificationDate());
        assertNotEquals(certBeforeTransaction.getPrice(), certAfterUpdate.getPrice());
        TestTransaction.end();
        Certificate certAfterTransaction = repository.getList(new CertificateSpecification(71), null, null).get(0);
        assertEquals(certBeforeTransaction.getName(), certAfterTransaction.getName());
        assertEquals(certBeforeTransaction.getModificationDate(), certAfterTransaction.getModificationDate());
        assertEquals(certBeforeTransaction.getPrice(), certAfterTransaction.getPrice());
    }

    @Test
    public void updateCertificateCommit() {
        Certificate certForUpdate = new Certificate();
        certForUpdate.setId(71);
        certForUpdate.setName("abc");
        certForUpdate.setPrice(BigDecimal.valueOf(15));
        certForUpdate.setExpirationDays(17);

        repository.update(certForUpdate);
        Certificate certAfterUpdate = repository.getList(new CertificateSpecification(71), null, null).get(0);
        assertNotEquals(certBeforeTransaction.getName(), certAfterUpdate.getName());
        assertNotEquals(certBeforeTransaction.getModificationDate(), certAfterUpdate.getModificationDate());
        assertNotEquals(certBeforeTransaction.getPrice(), certAfterUpdate.getPrice());
        TestTransaction.flagForCommit();
        TestTransaction.end();
        Certificate certAfterTransaction = repository.getList(new CertificateSpecification(71), null, null).get(0);
        assertNotEquals(certBeforeTransaction.getName(), certAfterTransaction.getName());
        assertNotEquals(certBeforeTransaction.getModificationDate(), certAfterTransaction.getModificationDate());
        assertNotEquals(certBeforeTransaction.getPrice(), certAfterTransaction.getPrice());
    }

    @Test
    public void deleteCertificateRollback() {
        System.out.println(repository.getList(new CertificateSpecification(71), null, null).get(0));
        repository.delete(71);
        assertNumCerts(count - 1);
        TestTransaction.end();
        assertNumCerts(count);
    }

    @Test
    public void deleteCertificateCommit() {
        repository.delete(1);

        TestTransaction.flagForCommit();
        TestTransaction.end();
        assertFalse(TestTransaction.isActive());
        assertNumCerts(count - 1);
    }

    protected int countRowsInTable(Object object) {
        Query query = entityManager.createQuery("SELECT count(t) FROM " + object.getClass().getSimpleName() + " t");
        return (int) (long) query.getSingleResult();
    }

    protected void assertNumCerts(int expected) {
        assertEquals("Number of rows in the gift_certificate table.", expected, countRowsInTable(new Certificate()));
    }
}
