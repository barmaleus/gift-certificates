package by.rekuts.giftcertificates.repository;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.specs.CertificateSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class, RepositoryConfig.class})
@ActiveProfiles("debug")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class CertificateRepositoryTest {
    @Autowired
    private CertificateRepository repository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    public void certRepositoryShouldNotBeNull() {
        Assert.assertNotNull(repository);
    }

    @Test
    public void getCertByIdTestTrue() {
        List<Certificate> singletonList = repository.getList(new CertificateSpecification(1), null, null);
        Certificate certificate = singletonList.get(0);
        Assert.assertEquals(1, singletonList.size());
        Assert.assertEquals("Certificate for 50% sale for dress", certificate.getName());
    }

    @Test
    public void getCertByIdTestFalse() {
        List<Certificate> emptyList = repository.getList(new CertificateSpecification(2), null, null);
        Assert.assertEquals(0, emptyList.size());
    }

    @Test
    public void getAllCertsTestTrue() {
        List<Certificate> certificates = repository.getList(new CertificateSpecification(), null, null);
        Assert.assertEquals(2, certificates.size());
        Assert.assertEquals("Certificate for 50% sale for dress", certificates.get(0).getName());
        Assert.assertEquals(71, certificates.get(1).getId().intValue());
    }

    @Test
    public void getCertsByTagSearchTestTrue() {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("tag", new String[]{"wedding"});
            put("search", new String[]{"dress"});
        }};

        List<Certificate> certificates = repository.getList(new CertificateSpecification(tagRepository, params), null, null);
        Assert.assertEquals(1, certificates.size());
        Assert.assertEquals("Certificate for 50% sale for dress", certificates.get(0).getName());
        Assert.assertEquals(1, certificates.get(0).getId().intValue());
    }

    @Test
    public void getCertsByMultipleTagsTestTrue() {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("tag", new String[]{"tratatag", "tratata"});
        }};

        List<Certificate> certificates = repository.getList(new CertificateSpecification(tagRepository, params), null, null);
        Assert.assertEquals(1, certificates.size());
        Assert.assertEquals("Certificate for 50% sale for dress1", certificates.get(0).getName());
        Assert.assertEquals(71, certificates.get(0).getId().intValue());
    }

    @Test
    public void getCertsByMultipleTagsTestFalse() {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("tag", new String[]{"tratatag", "tratata", "wedding"});
        }};

        List<Certificate> certificates = repository.getList(new CertificateSpecification(tagRepository, params), null, null);
        Assert.assertEquals(0, certificates.size());
    }

    @Test
    public void getCertsByBadTagTest() {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("tag", new String[]{"wedding-wedding"});
        }};

        List<Certificate> certificates = repository.getList(new CertificateSpecification(tagRepository, params), null, null);
        Assert.assertEquals(0, certificates.size());
    }

    @Test
    public void getCertsBySearchTestTrue() {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("search", new String[]{"dress1"});
        }};
        CertificateSpecification specification = new CertificateSpecification(tagRepository, params);
        List<Certificate> certificates = repository.getList(specification, null, null);
        Assert.assertEquals(1, certificates.size());
        Assert.assertEquals("Certificate for 50% sale for dress1", certificates.get(0).getName());
        Assert.assertEquals(71, certificates.get(0).getId().intValue());
    }

    @Test
    public void getCertsBySearchTestTrue2() {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("search", new String[]{"dress"});
        }};
        CertificateSpecification specification = new CertificateSpecification(tagRepository, params);
        List<Certificate> certificates = repository.getList(specification, null, null);
        Assert.assertEquals(2, certificates.size());
    }

    @Test
    public void getCertsByTagSearchTestFalse() {
        Map<String, String[]> params = new HashMap<String, String[]>() {{
            put("tag", new String[]{"funny"});
        }};
        CertificateSpecification specification = new CertificateSpecification(tagRepository, params);
        List<Certificate> certificates = repository.getList(specification, null, null);
        Assert.assertEquals(0, certificates.size());
    }

    @Test
    public void createCertTestTrue() {
        Certificate certificate = new Certificate();
        certificate.setName("Yet another certificate");
        certificate.setPrice(BigDecimal.TEN);
        String createdCertificatePath = repository.create(certificate);
        Assert.assertTrue(createdCertificatePath.endsWith("72"));
    }

    @Test(expected = PersistenceException.class)
    public void createCertNullPriceTestFalse() {
        Certificate certificate = new Certificate();
        certificate.setName("Yet another certificate");
        repository.create(certificate);
    }

    @Test(expected = PersistenceException.class)
    public void createCertNullNameTestFalse() {
        Certificate certificate = new Certificate();
        certificate.setPrice(BigDecimal.TEN);
        repository.create(certificate);
    }

    @Test
    public void deleteCertWithNoDependenciesTestTrue() {
        int originListSize = repository.getList(new CertificateSpecification(), null, null).size();
        repository.delete(71);
        int changedListSize = repository.getList(new CertificateSpecification(), null, null).size();
        Assert.assertEquals(1, originListSize - changedListSize);
    }

    @Test
    public void cascadeDeleteCertTestTrue() {
        int originListSize = repository.getList(new CertificateSpecification(), null, null).size();
        repository.delete(1);
        int changedListSize = repository.getList(new CertificateSpecification(), null, null).size();
        Assert.assertEquals(1, originListSize - changedListSize);
    }

    @Test
    public void updateCertTestTrue() {
        Certificate certificate = repository.getList(new CertificateSpecification(1), null, null).get(0);
        String oldName = certificate.getName();
        certificate.setName("New name");
        repository.update(certificate);
        Certificate modifiedCertificate = repository.getList(new CertificateSpecification(1), null, null).get(0);
        Assert.assertNotNull(modifiedCertificate.getModificationDate());
        Assert.assertNotEquals(oldName, modifiedCertificate.getName());
        Assert.assertEquals("New name", modifiedCertificate.getName());
    }

    @Test(expected = PersistenceException.class)
    public void updateCertTestFalse() {
        Certificate certificate = repository.getList(new CertificateSpecification(1), null, null).get(0);
        certificate.setName(null);
        repository.update(certificate);
    }
}
