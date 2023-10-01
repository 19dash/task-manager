package main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.model.Project;
import main.model.Task;
import main.repository.ProjectRepository;
import main.repository.TaskRepository;

@Service
public class ProjectService {
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private TaskRepository taskRepository;
	
	public void addProject(Project p) {
		p.getUsers().add(userService.getSessionUser());
		updateProject(p);
		System.out.println(p.getUsers());
	}
	public void updateProject(Project p) {
		projectRepository.save(p);
	}
	public void deleteProject(Project p) {
		List<Task>tasks=new ArrayList<>(taskRepository.findAll());
		for (Task t : tasks) {
		    t.setProject(null);
		}
		projectRepository.delete(p);
	}
	public Project getProject(Long id) {
		return projectRepository.findProjectById(id);
	}
	public List<Project> getUserProjects() {
		return projectRepository.
			findUserProjects(userService.getSessionUser().getId());
	}
	public List<Task> getProjectTasks(Project p) {
		return taskRepository.findByProjectId(p.getId());
	}
}
