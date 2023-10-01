package main.dto;

import main.auxiliary.Roles;

public class RoleDto {
	private Long id;
	private Roles role;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Roles getRole() {
		return role;
	}
	public void setRole(Roles role) {
		this.role = role;
	}
	
}
