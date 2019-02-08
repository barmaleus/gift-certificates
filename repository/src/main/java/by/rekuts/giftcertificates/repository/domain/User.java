package by.rekuts.giftcertificates.repository.domain;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "gift_user")
@TypeDef(
        name = "role_enum",
        typeClass = RoleEnumType.class
)
public class User implements Serializable {
	@Id
	@SequenceGenerator( name = "userSequence", sequenceName = "gift_user_id_seq", allocationSize = 1)
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "userSequence")
	private int id;

	@Column(nullable = false, unique = true)
	private String login;

	@Column(nullable = false)
	private String password;

	@Type(type="role_enum")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "role")
	private UserRole role;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(
			name = "gift_certificate",
			joinColumns = { @JoinColumn(name = "user_id") },
			inverseJoinColumns = { @JoinColumn(name = "certificate_id") }
	)
	private List<Certificate> certificates;

	public User(int id, String login, String password) {
		this.id = id;
		this.login = login;
		this.password = password;
	}

	public User() {
	}

	public enum UserRole {
		USER,
		ADMIN
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", login='" + login + '\'' +
				", password='" + password + '\'' +
				", role=" + role +
				'}';
	}
}
