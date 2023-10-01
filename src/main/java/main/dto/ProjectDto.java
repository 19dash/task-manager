package main.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class ProjectDto {
	private Long id;
	@NotBlank(message="Поле с названием должно быть заполнено")
	private String name;
	private List<TaskDto> tasks=new ArrayList<>();
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
	public List<TaskDto> getTasks() {
		return tasks;
	}
	
}
