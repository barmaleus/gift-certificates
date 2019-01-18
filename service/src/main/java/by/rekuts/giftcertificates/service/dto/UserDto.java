package by.rekuts.giftcertificates.service.dto;

import org.springframework.hateoas.ResourceSupport;

public class UserDto extends ResourceSupport {
    //todo пока отставил
    private int userId;
    private String login;
    private String password;

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

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
