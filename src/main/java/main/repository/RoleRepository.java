package main.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.auxiliary.Roles;
import main.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	public Role findRoleByRole(Roles role);
}