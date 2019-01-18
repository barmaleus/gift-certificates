package by.rekuts.giftcertificates.service.converter;

import by.rekuts.giftcertificates.repository.domain.User;
import by.rekuts.giftcertificates.service.dto.UserDto;

public class UserConverter implements CommonConverter<User, UserDto>{
    @Override
    public User dtoConvert(UserDto userDto) {
        return new User(userDto.getUserId(), userDto.getLogin(), userDto.getPassword());
    }

    @Override
    public UserDto domainConvert(User user) {
        return new UserDto(user.getId(), user.getLogin(), user.getPassword());
    }
}
