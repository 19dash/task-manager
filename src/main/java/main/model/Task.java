package main.model;

import main.auxiliary.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private Integer priority;
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	private LocalDate dueDate;
	private LocalDate updatedAt;
	@ManyToOne
	private User executor;
	@ManyToOne
	private User creator;
	@ManyToMany
	@JoinTable(
			name = "favorites", 
			joinColumns = @JoinColumn(name = "task_id"), 
			inverseJoinColumns = @JoinColumn(name = "user_id")
		)
	private List<User>
	favoriteToUsers;
	@ManyToOne
	private Project project;
	@ManyToOne 
	Group group;
	@OneToMany(mappedBy = "task", cascade=CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public User getExecutor() {
		return executor;
	}

	public void setExecutor(User executor) {
		this.executor = executor;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public List<User> getFavoriteToUsers() {
		return favoriteToUsers;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(
			LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
}
