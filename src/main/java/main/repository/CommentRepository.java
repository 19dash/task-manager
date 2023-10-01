package main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import main.model.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
	@Query(value = "SELECT u.name FROM users u " + "JOIN comments c ON u.id = c.users_id "
			+ "WHERE c.users_id = :id", nativeQuery = true)
	List<String> findUserOfComment(Long id);
}
