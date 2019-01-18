package by.rekuts.giftcertificates.repository.repos.impl;


import by.rekuts.giftcertificates.repository.domain.User;
import by.rekuts.giftcertificates.repository.repos.UserRepository;
import by.rekuts.giftcertificates.repository.specs.Specification;
import by.rekuts.giftcertificates.repository.specs.UserSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String BASE_USER_PATH = "/user";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String create(User user) {
        if(user.getRole() == null) {
            user.setRole(User.UserRole.USER);
        }
        entityManager.persist(user);
        entityManager.flush();
        return BASE_USER_PATH + "/" + user.getId();
    }

    @Override
    public void update(User user) {
        if(user.getRole() == null || user.getRole() == User.UserRole.ADMIN) {
            User tempUser = getList(new UserSpecification(user.getId())).get(0);
            user.setRole(tempUser.getRole());
        }
        User dbUser = entityManager.find(User.class, user.getId());
        dbUser.setLogin(user.getLogin());
        dbUser.setPassword(user.getPassword());
        dbUser.setRole(user.getRole());
        entityManager.merge(dbUser);
    }

    @Override
    public void delete(int id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public List<User> getList(Specification specification) {
        UserSpecification userSpecification = (UserSpecification) specification;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        List<Predicate> predicates;
        predicates = userSpecification.getPredicates(root, builder);

        if (!predicates.isEmpty()) {
            criteriaQuery.where(
                    predicates.toArray(new Predicate[]{})
            );
        }
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
