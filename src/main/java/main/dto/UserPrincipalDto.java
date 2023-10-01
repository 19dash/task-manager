package main.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import main.security.validators.PasswordMatches;
import main.security.validators.ValidEmail;

@PasswordMatches
public class UserPrincipalDto implements UserDetails{
	private static final long serialVersionUID = 1L;
	String name;
	private Long id;
	@ValidEmail()
	@NotBlank(message="Поле не должно быть пустым")
	@NotEmpty
	private String username;
	@NotBlank(message="Пароли не совпадают")
	private String password;
	private String matchingPassword;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = 
				new ArrayList<GrantedAuthority>();
		return list;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String setUsername(String username) {
		return this.username=username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String getUsername() {
		return username;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	/*public void setUsername(String username) {
		this.username = username;
	}*/

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
