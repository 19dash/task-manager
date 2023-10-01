package main.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.auxiliary.filter.Filter;
import main.model.Comment;
import main.model.Task;
import main.repository.CommentRepository;
import main.repository.TaskRepository;

@Service
public class TaskService {
	@Autowired
	private UserService userService;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private CommentRepository commentRepository;

	public void addTask(Task task) {
		task.setCreator(userService.getSessionUser());
		updateTask(task);
	}
	public void updateTask(Task task) {
		task.setUpdatedAt(LocalDate.now());
		taskRepository.save(task);
	}
	public Task getTask(Long id) {
		return taskRepository.findTaskById(id);
	}
	public void deleteTask(Task task) {
		taskRepository.delete(task);
	}
	public boolean isFavorite(Task task) {
		return taskRepository
			.findFavoriteByTaskId(task.getId())!=null;
	}
	public void addToFavorite(Task task) {
		/*taskRepository
		.insertFavoriteByTaskIdAndUserId
		(task.getId(), userService.getSessionUser().getId());*/
		task.getFavoriteToUsers().
		add(userService.getSessionUser());
		taskRepository.save(task);
		System.out.println("ADDED");
	}
	public void deleteFromFavorite(Task task) {
		/*taskRepository.
		deleteFavoriteByTaskIdAndUserId
		(task.getId(), userService.getSessionUser().getId());*/
		task.getFavoriteToUsers().remove(userService.getSessionUser());
		taskRepository.save(task);
	}
	public List<Task> getUserFavorites() {
		return
		taskRepository
		.findUserFavorites(userService.getSessionUser().getId());
	}
	
	//задания, где пользователь - исполнотель и/или поручитель
	public List<Task> getUserTasks() {
		return taskRepository.
		findByExecutorIdOrCreatorId
		(userService.getSessionUser().getId(),
				userService.getSessionUser().getId());
	}
	public void addComment(Task task, Comment comment) {
		comment.setUser(userService.getSessionUser());
		comment.setTask(task);
		commentRepository.save(comment);
	}
	public List<Task> filter(Filter filter) {
		return taskRepository.findAll(filter);
	}
}
