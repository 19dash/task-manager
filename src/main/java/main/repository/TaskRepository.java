package main.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import main.model.Task;

@Repository
public interface TaskRepository 
extends JpaRepository<Task, Long>, 
JpaSpecificationExecutor<Task> {
	@Query(value = "SELECT t.* FROM tasks t " + "JOIN task_members tm ON t.id = tm.task_id "
			+ "JOIN members m ON m.id=tm.members_id " + "WHERE m.executor_id = :id", nativeQuery = true)
	List<Task> findUserTasks(Long id);

	
	List<Task> findByProjectId(Long id);

	@Query(value = "SELECT t.* FROM favorites f " + "JOIN tasks t ON t.id = f.task_id "
			+ "WHERE f.user_id = :id", nativeQuery = true)
	List<Task> findUserFavorites(Long id);

	Task findTaskById(Long id);

	@Query(value = "SELECT t.* FROM favorites f " + "JOIN tasks t ON t.id = f.task_id "
			+ "WHERE f.task_id = :id", nativeQuery = true)
	Task findFavoriteByTaskId(Long id);

	List<Task> findAllByOrderByDueDateDesc();
	
	@Query(value = "delete FROM favorites "
	+ "WHERE task_id=:taskId AND user_id=:userId",
	nativeQuery = true)
	void deleteFavoriteByTaskIdAndUserId(Long taskId, Long userId);
	@Modifying
	@Query(value = "insert into favorites(task_id, user_id) "
	+ "values(:taskId, :userId)",
	nativeQuery = true)
	void insertFavoriteByTaskIdAndUserId(Long taskId, Long userId);
	List<Task> findByDueDate(Date date);
	List<Task> findByExecutorId(Long id);
	List<Task> findByExecutorIdOrCreatorId(Long id1, Long id2);
}
