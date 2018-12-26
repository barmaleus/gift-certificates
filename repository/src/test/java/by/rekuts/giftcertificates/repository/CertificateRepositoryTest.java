package by.rekuts.giftcertificates.repository;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.specs.AllCertsSpecification;
import by.rekuts.giftcertificates.repository.specs.OneCertificateByIdSpecification;
import by.rekuts.giftcertificates.repository.specs.SearchCertsSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryConfig.class)
@ActiveProfiles("debug")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CertificateRepositoryTest {
    @Autowired
    private CertificateRepository repository;

    @Test
    public void certRepositoryShouldNotBeNull() {
        Assert.assertNotNull(repository);
    }

    @Test
    public void getCertByIdTestTrue() {
        OneCertificateByIdSpecification specification = new OneCertificateByIdSpecification(String.valueOf(1));
        List<Certificate> singletonList = repository.getList(specification);
        Certificate certificate = singletonList.get(0);
        Assert.assertEquals(1, singletonList.size());
        Assert.assertEquals("Certificate for 50% sale for dress", certificate.getName());
    }

    @Test
    public void getCertByIdTestFalse() {
        OneCertificateByIdSpecification specification = new OneCertificateByIdSpecification(String.valueOf(2));
        List<Certificate> emptyList = repository.getList(specification);
        Assert.assertEquals(0, emptyList.size());
    }

    @Test
    public void getAllCertsTestTrue() {
        AllCertsSpecification specification = new AllCertsSpecification();
        List<Certificate> certificates = repository.getList(specification);
        Assert.assertTrue(certificates.size() == 2);
        Assert.assertEquals("Certificate for 50% sale for dress", certificates.get(0).getName());
        Assert.assertEquals(71, certificates.get(1).getCertificateId());
    }

    @Test
    public void getCertsByTagSearchTestTrue() {
        Map<String, String> params = new HashMap<String, String>() {{
            put("tag", "wedding");
            put("search", "dress");
        }};
        SearchCertsSpecification specification = new SearchCertsSpecification(params);
        List<Certificate> certificates = repository.getList(specification);
        Assert.assertEquals(1, certificates.size());
        Assert.assertEquals("Certificate for 50% sale for dress", certificates.get(0).getName());
        Assert.assertEquals(1, certificates.get(0).getCertificateId());
    }

    @Test
    public void getCertsBySearchTestTrue() {
        Map<String, String> params = new HashMap<String, String>() {{
            put("search", "dress1");
        }};
        SearchCertsSpecification specification = new SearchCertsSpecification(params);
        List<Certificate> certificates = repository.getList(specification);
        Assert.assertEquals(1, certificates.size());
        Assert.assertEquals("Certificate for 50% sale for dress1", certificates.get(0).getName());
        Assert.assertEquals(71, certificates.get(0).getCertificateId());
    }

    @Test
    public void getCertsBySearchTestTrue2() {
        Map<String, String> params = new HashMap<String, String>() {{
            put("search", "dress");
        }};
        SearchCertsSpecification specification = new SearchCertsSpecification(params);
        List<Certificate> certificates = repository.getList(specification);
        Assert.assertEquals(2, certificates.size());
    }

    @Test
    public void getCertsByTagSearchTestFalse() {
        Map<String, String> params = new HashMap<String, String>() {{
            put("tag", "funny");
        }};
        SearchCertsSpecification specification = new SearchCertsSpecification(params);
        List<Certificate> certificates = repository.getList(specification);
        Assert.assertEquals(0, certificates.size());
    }

    @Test
    public void createCertTestTrue() {
        Certificate certificate = new Certificate();
        certificate.setName("Yet another certificate");
        certificate.setPrice(BigDecimal.TEN);
        String createdCertificatePath = repository.create(certificate);
        Assert.assertFalse(createdCertificatePath.endsWith("-1"));
        Assert.assertTrue(createdCertificatePath.endsWith("72"));
    }

    @Test
    public void createCertNullPriceTestFalse() {
        Certificate certificate = new Certificate();
        certificate.setName("Yet another certificate");
        String createdCertificatePath = repository.create(certificate);
        System.out.println(createdCertificatePath);
        Assert.assertTrue(createdCertificatePath.endsWith("-1"));
    }

    @Test
    public void createCertNullNameTestFalse() {
        Certificate certificate = new Certificate();
        certificate.setPrice(BigDecimal.TEN);
        String createdCertificatePath = repository.create(certificate);
        System.out.println(createdCertificatePath);
        Assert.assertTrue(createdCertificatePath.endsWith("-1"));
    }

    @Test
    public void deleteCertWithNoDependenciesTestTrue() {
        int originListSize = repository.getList(new AllCertsSpecification()).size();
        repository.delete(71);
        int changedListSize = repository.getList(new AllCertsSpecification()).size();
        Assert.assertEquals(1, originListSize - changedListSize);
    }

    @Test
    public void cascadeDeleteCertTestTrue() {
        int originListSize = repository.getList(new AllCertsSpecification()).size();
        repository.delete(1);
        int changedListSize = repository.getList(new AllCertsSpecification()).size();
        Assert.assertEquals(1, originListSize - changedListSize);
    }

}
