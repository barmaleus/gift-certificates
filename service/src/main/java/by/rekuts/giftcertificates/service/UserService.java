package by.rekuts.giftcertificates.service;

import by.rekuts.giftcertificates.service.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends CrudService<UserDto>, UserDetailsService {
    UserDto getUserById(int id);
    UserDto getUserByLogin(String login);
    boolean buyCertificate(String username, int certId) throws ServiceException;
}
