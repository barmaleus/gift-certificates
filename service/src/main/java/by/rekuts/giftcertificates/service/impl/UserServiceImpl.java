package by.rekuts.giftcertificates.service.impl;

import by.rekuts.giftcertificates.repository.domain.Certificate;
import by.rekuts.giftcertificates.repository.domain.Purchase;
import by.rekuts.giftcertificates.repository.domain.Tag;
import by.rekuts.giftcertificates.repository.domain.User;
import by.rekuts.giftcertificates.repository.repos.CertificateRepository;
import by.rekuts.giftcertificates.repository.repos.PurchaseRepository;
import by.rekuts.giftcertificates.repository.repos.TagRepository;
import by.rekuts.giftcertificates.repository.repos.UserRepository;
import by.rekuts.giftcertificates.repository.specs.PurchaseSpecification;
import by.rekuts.giftcertificates.repository.specs.UserSpecification;
import by.rekuts.giftcertificates.service.ServiceException;
import by.rekuts.giftcertificates.service.UserService;
import by.rekuts.giftcertificates.service.converter.TagConverter;
import by.rekuts.giftcertificates.service.converter.UserConverter;
import by.rekuts.giftcertificates.service.dto.TagDto;
import by.rekuts.giftcertificates.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PurchaseRepository purchaseRepository;
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final UserConverter converter;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PurchaseRepository purchaseRepository, CertificateRepository certificateRepository, TagRepository tagRepository, UserConverter converter, PasswordEncoder encoder) {
        this.repository = repository;
        this.purchaseRepository = purchaseRepository;
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.encoder = encoder;
        this.converter = converter;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> singletonUserList = repository.getList(new UserSpecification(username), null, null);
        if(singletonUserList.isEmpty()) {
            throw new UsernameNotFoundException(username + "not found");
        }
        User user = singletonUserList.get(0);
        UsernamePasswordAuthenticationToken authenticationToken
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        authenticationToken.getAuthorities();
//        System.out.println("blabla|| " + authenticationToken.getAuthorities()); //todo do
//        System.out.println(authenticationToken);
//        if (user.getRole().name().equals("ADMIN")) {
//        }
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
        if(userDto.getLogin() != null && userDto.getPassword() != null) {
            UserDto tempUser = getUserById(userDto.getUserId());
            userDto.setUserId(tempUser.getUserId());
            userDto.setPassword(encoder.encode(userDto.getPassword()));
            repository.update(converter.dtoConvert(userDto));
        } else {
            throw new ServiceException("Can't update user. Login, password is expected.");
        }
    }

    @Transactional
    @Override
    public void delete(int id) {
        repository.delete(id);
    }

    @Override
    public List<UserDto> getList(Integer page, Integer itemsPerPage) {
        return repository
                .getList(new UserSpecification(), page, itemsPerPage)
                .stream()
                .map(converter::domainConvert)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(int id) {
        return repository
                .getList(new UserSpecification(id), null, null)
                .stream()
                .map(converter::domainConvert)
                .findFirst()
                .orElse(null);
    }

    @Override
    public UserDto getUserByLogin(String login) {
        return repository
                .getList(new UserSpecification(login), null, null)
                .stream()
                .map(converter::domainConvert)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Map.Entry<String, BigDecimal> getMostPopularUsersTag(Integer userId) {
        UserDto user = getUserById(userId);
        List<Purchase> purchaseList = purchaseRepository
                .getList(new PurchaseSpecification(userId, null), null, null);

        Map<Certificate, BigDecimal> certificateMap = purchaseList.stream()
                .collect(Collectors.toMap(Purchase::getCertificate, Purchase::getPrice));

        Map<Tag, BigDecimal> tagMap = new HashMap<>();

        certificateMap.forEach((certificate, price) -> {
            for(Tag tag : certificate.getTags()) {
                if (tagMap.containsKey(tag)) {
                    tagMap.put(tag, tagMap.get(tag).add(price));
                } else {
                    tagMap.put(tag, price);
                }
            }
        });

        Map.Entry<String, BigDecimal> maxEntry = new AbstractMap.SimpleEntry<>("There are no popular tags yet", BigDecimal.valueOf(-1));

        for (Map.Entry<Tag, BigDecimal> entry : tagMap.entrySet()) {
            if (entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = new AbstractMap.SimpleEntry<>(entry.getKey().getName(), entry.getValue());

            }
        }

        return maxEntry;
    }
}
