package main.auxiliary.filter;

import java.time.LocalDate;

import main.dto.GroupDto;
import main.dto.ProjectDto;
import main.dto.UserDto;

public class FilterObject {
	private UserDto executor;
	private ProjectDto project;
	private GroupDto group;
	private LocalDate dueDate;
	private String sortByDate;
	private String keyword;
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
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public String getSortByDate() {
		return sortByDate;
	}
	public void setSortByDate(String sortByDate) {
		this.sortByDate = sortByDate;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public GroupDto getGroup() {
		return group;
	}
	public void setGroup(GroupDto group) {
		this.group = group;
	}
	
}
