package main.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;
import main.dto.ProjectDto;
import main.dto.TaskDto;
import main.model.Project;
import main.service.ProjectService;

@Controller
public class ProjectController {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProjectService projectService;

	@GetMapping("/addproject")
	public String viewProjectForm(Model model) {
		model.addAttribute("project", new ProjectDto());
		model.addAttribute("path", "/addproject");
		model.addAttribute("header", "Добавить проект");
		return "project-form";
	}

	@PostMapping("/addproject")
	public String sendProjectForm(
			@ModelAttribute("project") 
			@Valid ProjectDto projectDto,
			BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "project-form";
        }
		Project project = modelMapper.map(projectDto, 
				Project.class);
		projectService.addProject(project);
		return "redirect:/projects";
	}
	
	@GetMapping("/projects")
	public String viewProjectList(Model model) {
		ArrayList<Project> projects = 
			new ArrayList<>(projectService.getUserProjects());
		ArrayList<ProjectDto> projectDtos = 
		modelMapper.map(projects, 
		new TypeToken<ArrayList<ProjectDto>>() {}.getType());
		model.addAttribute("projects", projectDtos);
		return "project-list";
	}

	@GetMapping("/projects/{id}")
	public String viewProject(@PathVariable Long id, Model model) {
		Project project = projectService.getProject(id);
		ProjectDto projectDto = 
				modelMapper.map(project, ProjectDto.class);

		ArrayList<TaskDto> taskDtos = 
				modelMapper.map(project.getTasks(), 
				new TypeToken<ArrayList<TaskDto>>() {}.getType());
		projectDto.getTasks().addAll(taskDtos);
		model.addAttribute("project", projectDto);
		return "project-view";
	}
	@RequestMapping(value = "/projects/{id}", 
			params = "edit", method = RequestMethod.POST)
	public String editProject(@PathVariable Long id, Model model) {
	
		return "redirect:/projects/"+id+"/edit";
	}
	@RequestMapping(value = "/projects/{id}", 
			params = "deleteProject", method = RequestMethod.POST)
	public String deleteProject(@PathVariable Long id, Model model) {
		Project project = projectService.getProject(id);
		projectService.deleteProject(project);
		return "redirect:/projects";
	}
	@GetMapping("/projects/{id}/edit")
	public String viewEditProjectForm(@PathVariable Long id, 
			Model model) {
		Project project = projectService.getProject(id);
		ProjectDto projectDto = 
				modelMapper.map(project, ProjectDto.class);
		model.addAttribute("project", projectDto);
		model.addAttribute("path", "/projects/"+id+"/edit");
		model.addAttribute("header", "Редактировать проект");
		return "project-form";
	}
	@PostMapping("/projects/{id}/edit")
	public String sendEditProjectForm(@PathVariable Long id,
			@ModelAttribute("project") ProjectDto projectDto,
			Model model) {
		
		Project project = modelMapper.map(projectDto, 
				Project.class);
		projectService.updateProject(project);
		return "redirect:/projects/"+id;
	}
}
