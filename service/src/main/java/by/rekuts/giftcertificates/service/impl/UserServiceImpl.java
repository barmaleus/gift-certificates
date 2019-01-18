package by.rekuts.giftcertificates.service.impl;

import by.rekuts.giftcertificates.repository.domain.User;
import by.rekuts.giftcertificates.repository.repos.UserRepository;
import by.rekuts.giftcertificates.repository.specs.UserSpecification;
import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.UserService;
import by.rekuts.giftcertificates.service.converter.UserConverter;
import by.rekuts.giftcertificates.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserConverter converter;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserConverter converter, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
        this.converter = converter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> singletonUserList = repository.getList(new UserSpecification(username));
        if(singletonUserList.size() == 0) {
            throw new UsernameNotFoundException(username + "not found");
        }
        User user = singletonUserList.get(0);
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()))
        );
    }

    @Transactional
    @Override
    public String create(UserDto userDto) throws ServiceException {
        if(userDto.getLogin() != null && userDto.getPassword() != null) {
            userDto.setPassword(encoder.encode(userDto.getPassword()));
            userDto.setPassword(userDto.getPassword());
            return repository.create(converter.dtoConvert(userDto));
        } else {
            throw new ServiceException("Can't create new user. Login or password is expected.");
        }
    }

    @Transactional
    @Override
    public void update(UserDto userDto) throws ServiceException {
        if(userDto.getLogin() != null && userDto.getPassword() != null && userDto.getUserId() != 0) {
            UserDto tempUser = getUserById(userDto.getUserId());
            // check, if password has changed
            if(userDto.getPassword().equals(tempUser.getPassword())) {
                repository.update(converter.dtoConvert(userDto));
            } else {
                userDto.setPassword(encoder.encode(userDto.getPassword()));
                userDto.setPassword(userDto.getPassword());
                repository.update(converter.dtoConvert(userDto));
            }
        } else {
            throw new ServiceException("Can't update user. Login, password or id is expected.");
        }
    }

    @Transactional
    @Override
    public void delete(int id) {
        repository.delete(id);
    }

    @Override
    public List<UserDto> getList() {
        List<User> users = repository.getList(new UserSpecification());
        return users.stream()
                .map(converter::domainConvert)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(int id) {
        List<User> users = repository.getList(new UserSpecification(id));
        return getUserDtoFromSingletonUserList(users);
    }

    @Override
    public UserDto getUserByLogin(String login) {
        List<User> users = repository.getList(new UserSpecification(login));
        return getUserDtoFromSingletonUserList(users);
    }

    private UserDto getUserDtoFromSingletonUserList(List<User> users) {
        if(users.size() != 0) {
            User user = users.get(0);
            return converter.domainConvert(user);
        } else {
            return null;
        }
    }
}
