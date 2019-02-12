package by.rekuts.giftcertificates.service.converter;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.domain.User;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.specs.CertificateSpecification;
import by.rekuts.giftcertificates.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserConverter implements CommonConverter<User, UserDto> {

    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public User dtoConvert(UserDto userDto) {
        User user = new User(userDto.getUserId(), userDto.getLogin(), userDto.getPassword());
        List<Certificate> certificates = userDto.getCertificates()
                .stream()
                .map(certId -> certificateRepository
                        .getList(new CertificateSpecification(certId), null, null)
                        .stream()
                        .findFirst().orElse(null))
                .filter(Objects::isNull)
                .collect(Collectors.toList());
        user.setCertificates(certificates);
        return user;
    }

    @Override
    public UserDto domainConvert(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getLogin(), user.getPassword());
        userDto.setCertificates(user.getCertificates()
                .stream()
                .map(Certificate::getId)
                .collect(Collectors.toList()));
        return userDto;
    }
}
