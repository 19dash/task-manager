package main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import main.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
	@Query(value = "SELECT g.* FROM groups g JOIN "
	+ "	members m ON g.id=m.group_id where m.user_id=:userId", nativeQuery = true)
	List<Group> findUserGroups(Long userId);

	@Query(value = "SELECT g.* FROM groups g JOIN " 
			+ "members m ON g.id=m.group_id JOIN roles r "
			+ "ON m.user_id=:userId AND r.role=:role", nativeQuery = true)
	List<Group> findUserGroupsOfRole(Long userId, String role);

	Group findGroupById(Long id);
	
}
