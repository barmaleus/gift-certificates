package by.rekuts.giftcertificates.service;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.repos.impl.CertificateRepositoryImpl;
import by.rekuts.giftcertificates.repository.repos.impl.TagRepositoryImpl;
import by.rekuts.giftcertificates.repository.specs.CertificateSpecification;
import by.rekuts.giftcertificates.service.converter.CertificateConverter;
import by.rekuts.giftcertificates.service.dto.CertificateDto;
import by.rekuts.giftcertificates.service.impl.CertificateServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class CertificateCrudServiceTest {
    private CertificateRepository repository = mock(CertificateRepositoryImpl.class);
    private TagRepository tagRepository = mock(TagRepositoryImpl.class);
    private CertificateConverter converter = new CertificateConverter();
    private CertificateService service = new CertificateServiceImpl(repository, tagRepository, converter);
    private List<CertificateDto> certDtos;

    @Before
    public void init() {
        CertificateDto certificate1 = new CertificateDto();
        certificate1.setCertificateId(1);
        certificate1.setName("Certificate 1");
        certificate1.setPrice(BigDecimal.TEN);
        CertificateDto certificate2 = new CertificateDto();
        certificate2.setCertificateId(8);
        certificate2.setName("Certificate 2");
        certificate2.setPrice(BigDecimal.valueOf(5L));
        certDtos = Arrays.asList(certificate1, certificate2);
    }

    @Test
    public void createCertificateTestTrue() throws ServiceException {
        when(repository.create(any(Certificate.class))).thenReturn("/certificate/" + certDtos.get(0).getCertificateId());
        String path = service.create(certDtos.get(0));
        service.create(certDtos.get(1));
        verify(repository, times(2)).create(any());
        assertEquals("/certificate/1", path);
    }

    @Test (expected = ServiceException.class)
    public void createCertificateTestFalse() throws ServiceException {
        CertificateDto certDto = new CertificateDto();
        service.create(certDto);
    }

    @Test
    public void updateCertificateTestTrue() throws ServiceException {
        doNothing().when(repository).update(any(Certificate.class));
        service.update(certDtos.get(0));
        service.update(certDtos.get(1));
        verify(repository, times(2)).update(any());
    }

    @Test (expected = ServiceException.class)
    public void updateCertificateTestFalse() throws ServiceException {
        CertificateDto certDto = new CertificateDto();
        service.create(certDto);
    }

    @Test
    public void deleteCertificateTestTrue() {
        doNothing().when(repository).delete(anyInt());
        service.delete(0);
        service.delete(1);
        verify(repository, Mockito.times(2)).delete(anyInt());
    }

    @Test
    public void getAllCertsTestTrue() {
        when(repository.getList(any(CertificateSpecification.class), eq(null), eq(null)))
                .thenReturn(
                        certDtos.stream()
                                .map(converter::dtoConvert)
                                .collect(Collectors.toList())
                );
        List<CertificateDto> certs = service.getList(null, null);
        Assert.assertEquals(2, certs.size());
        Assert.assertEquals("Certificate 1", certs.get(0).getName());
        Assert.assertEquals("Certificate 2", certs.get(1).getName());
    }

    @Test
    public void getCertsByParamsTestTrue() {
        Map<String, String> params = new HashMap<String, String>() {{
            put("search", "Certificate 1");
        }};
        when(repository.getList(any(CertificateSpecification.class), eq(null), eq(null)))
                .thenReturn(
                    Collections.singletonList(certDtos.get(0))
                            .stream()
                            .map(el -> converter.dtoConvert(el))
                            .collect(Collectors.toList())
                );
        List<CertificateDto> certs = service.getList(params, null, null);
        Assert.assertEquals(1, certs.size());
        Assert.assertEquals("Certificate 1", certs.get(0).getName());
    }

    @Test
    public void getCertsByParamsTestTrue2() {
        Map<String, String> params = new HashMap<String, String>() {{
            put("search", "Certificate");
        }};
        when(repository.getList(any(CertificateSpecification.class), eq(null), eq(null)))
                .thenReturn(
                        certDtos
                                .stream()
                                .map(el -> converter.dtoConvert(el))
                                .collect(Collectors.toList())
                );
        List<CertificateDto> certs = service.getList(params, null, null);
        Assert.assertEquals(2, certs.size());
        Assert.assertEquals("Certificate 1", certs.get(0).getName());
        Assert.assertEquals("Certificate 2", certs.get(1).getName());
    }

    @Test
    public void getCertsByParamsTestTrue3() {
        Map<String, String> params = new HashMap<String, String>() {{
            put("search", "Certificate №1");
        }};
        when(repository.getList(any(CertificateSpecification.class), eq(null), eq(null)))
                .thenReturn(Collections.emptyList());
        List<CertificateDto> certs = service.getList(params, null, null);
        Assert.assertEquals(0, certs.size());
    }

    @Test
    public void getCertByIdTestTrue() {
        when(repository.getList(any(CertificateSpecification.class), eq(null), eq(null)))
                .thenReturn(Collections.singletonList(converter.dtoConvert(certDtos.get(1))));
        CertificateDto cert = service.getCertById(8);
        Assert.assertEquals(8, cert.getCertificateId());
    }

    @Test
    public void getCertByIdTestFalse() {
        when(repository.getList(any(CertificateSpecification.class), eq(null), eq(null)))
                .thenReturn(new ArrayList<>());
        CertificateDto cert = service.getCertById(9);
        Assert.assertNull(cert);
    }
}
