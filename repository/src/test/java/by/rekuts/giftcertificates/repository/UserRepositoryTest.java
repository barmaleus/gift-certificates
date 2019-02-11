package by.rekuts.giftcertificates.repository;

import by.rekuts.giftcertificates.repository.domain.User;
import by.rekuts.giftcertificates.repository.repos.UserRepository;
import by.rekuts.giftcertificates.repository.repos.impl.TagRepositoryImpl;
import by.rekuts.giftcertificates.repository.specs.UserSpecification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TagRepositoryImpl.class, TestConfig.class})
@ActiveProfiles("debug")
@Transactional
@DirtiesContext
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Test
    public void createUserTest() {
        User user = new User();
        user.setLogin("userok");
        user.setPassword("userokpass");
        repository.create(user);
        Assert.assertEquals("User{id=7, login='userok', password='userokpass', role=USER}", user.toString());
    }

    @Test (expected = PersistenceException.class)
    public void createUserTestFalseNoPassword() {
        User user = new User();
        user.setLogin("userok");
        repository.create(user);
    }

    @Test (expected = PersistenceException.class)
    public void createUserTestFalseNameIsNotUnique() {
        User user = new User();
        user.setLogin("user1");
        user.setPassword("userpass");
        repository.create(user);
    }

    @Test
    public void updateUserTest() {
        User user = new User(1, "userok1", "userokpass");
        repository.update(user);
        System.out.println(user.toString());
        Assert.assertEquals("User{id=1, login='userok1', password='userokpass', role=USER}", user.toString());
    }

    @Test
    public void deleteUserTest() {
        int beforeUsersAmount = repository.getList(new UserSpecification(), null, null).size();
        repository.delete(2);
        int afterUsersAmount = repository.getList(new UserSpecification(), null, null).size();
        Assert.assertEquals(1, beforeUsersAmount - afterUsersAmount);
    }
}
