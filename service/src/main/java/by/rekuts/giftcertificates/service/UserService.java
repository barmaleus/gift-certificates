package by.rekuts.giftcertificates.service;

import by.rekuts.giftcertificates.service.dto.TagDto;
import by.rekuts.giftcertificates.service.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.util.Map;

public interface UserService extends CrudService<UserDto>, UserDetailsService {
    UserDto getUserById(int id);
    UserDto getUserByLogin(String login);
    Map.Entry<String, BigDecimal> getMostPopularUsersTag(Integer userId);
}
