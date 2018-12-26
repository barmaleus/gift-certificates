package by.rekuts.giftcertificates.service.converter;

public interface GiftCertConverter<Domain, Dto> {
    Domain dtoConvert(Dto dto);

    Dto domainConvert(Domain domain);
}
