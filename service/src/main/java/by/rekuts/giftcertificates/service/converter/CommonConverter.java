package by.rekuts.giftcertificates.service.converter;

public interface CommonConverter<Domain, Dto> {
    Domain dtoConvert(Dto dto);

    Dto domainConvert(Domain domain);
}
