package by.rekuts.giftcertificates.repository.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification extends Specification {
    private Integer id;
    private String login;

    public UserSpecification() {}
    public UserSpecification(Integer id) {
        this.id = id;
    }
    public UserSpecification(String login) {
        this.login = login;
    }

    @Override
    public List<Predicate> getPredicates(Root root, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if (id != null) {
            predicates.add(
                    builder.equal(root.get("id"), id)
            );
        }
        if (login != null) {
            predicates.add(
                    builder.equal(root.get("login"), login)
            );
        }
        return predicates;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserSpecification{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}
