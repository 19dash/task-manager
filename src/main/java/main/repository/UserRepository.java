package main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import main.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findUserById(Long id);
	User findUserByEmail(String email);
	//@Query(value = "SELECT * FROM users WHERE login=:login AND " + "password=:password", nativeQuery = true)
	User findUserByEmailAndPassword(String login, String password);

	@Query(value = "SELECT u.* FROM users u JOIN "
			+ "members m ON m.user_id=u.id AND "
			+ "m.group_id=:groupId", nativeQuery = true)
	List<User> findUsersOfGroup(Long groupId);
	
	@Query(value = "SELECT * FROM users u where not exists "
			+ "(select * from members where group_id=:groupId "
			+ " and u.id=user_id)", nativeQuery = true)
	List<User> findUsersOutOfGroup(Long groupId);
	
	List<User> findByEmail(String email);
	User findUserByName(String name);
	@Query(value = "SELECT DISTINCT u.* FROM users u JOIN members m "
			+ "ON u.id=m.user_id", nativeQuery = true)
	List<User>findUniqueUsers();
	
}
