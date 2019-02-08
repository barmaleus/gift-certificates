package by.rekuts.giftcertificates.service.converter;

public interface CommonConverter<T, K> {
    T dtoConvert(K dto);

    K domainConvert(T domain);
}
