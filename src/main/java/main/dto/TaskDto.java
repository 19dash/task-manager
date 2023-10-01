package main.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import main.auxiliary.TaskStatus;

public class TaskDto {
	
	private Long id;
	@NotBlank(message="Поле с названием должно быть заполнено")
	private String name;
	private String description;
	private Integer priority;
	private TaskStatus status;
	@NotNull(message="Поле с дедлайном должно быть заполнено")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDate;
	private UserDto executor;
	private ProjectDto project;
	private GroupDto group;
	private List<CommentDto> comments=new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public List<CommentDto> getComments() {
		return comments;
	}

	public UserDto getExecutor() {
		return executor;
	}

	public void setExecutor(UserDto executor) {
		this.executor = executor;
	}

	public ProjectDto getProject() {
		return project;
	}

	public void setProject(ProjectDto project) {
		this.project = project;
	}

	public GroupDto getGroup() {
		return group;
	}

	public void setGroup(GroupDto group) {
		this.group = group;
	}
	

}
