//@Author Fabian Küffer 15-931-421

package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

@Entity
public class User implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
//	@JsonIgnore
	private String password;


	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
	private UserStatus status;

	//set the creationdate to "now"
	@Column(nullable = false)
	private LocalDateTime createdDate = LocalDateTime.now();

	@Column(nullable = true)
	private String birthday;

	//Client has games attribute, added here but might need to be in another entity later
	@Column(nullable = true)
	private Integer Losses;

	//Client has moves attribute, added here but might need to be in another entity later
	@Column (nullable = true)
	private Integer Wins;

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDate(){
		return this.createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}


	public Integer getWins() {
		return Wins;
	}

	public void setWins(Integer wins) {
		Wins = wins;
	}

	public Integer getLosses() {
		return Losses;
	}

	public void setLosses(Integer losses) {
		Losses = losses;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return this.getId().equals(user.getId());
	}

	@Override
	public int hashCode() {
		//for the equals method
		return Objects.hash(id, password, username, token, status, createdDate, birthday, Losses, Wins);
	}
//

	@Override
	public String toString() {
		//default, change later if needed!
		return super.toString();
	}
}
