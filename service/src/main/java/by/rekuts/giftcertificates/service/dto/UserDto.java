package by.rekuts.giftcertificates.service.dto;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class UserDto extends ResourceSupport {
    private int userId;
    private String login;
    private String password;
    private List<Integer> certificates;

    public UserDto() {}

    public UserDto(int userId, String login, String password) {
        this.userId = userId;
        this.login = login;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Integer> certificates) {
        this.certificates = certificates;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", certificates=" + certificates +
                '}';
    }
}
