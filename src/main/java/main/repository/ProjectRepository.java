package main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import main.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	@Query(value = "SELECT p.* FROM projects p " + "JOIN projects_users pu ON p.id = pu.projects_id "
			+ "WHERE pu.users_id = :id", nativeQuery = true)
	List<Project> findUserProjects(Long id);
	
	Project findProjectById(Long id);
}
