package main.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import main.auxiliary.filter.Condition;
import main.auxiliary.filter.Filter;
import main.auxiliary.filter.FilterObject;
import main.dto.CommentDto;
import main.dto.GroupDto;
import main.dto.ProjectDto;
import main.dto.TaskDto;
import main.dto.UserDto;
import main.model.Comment;
import main.model.Group;
import main.model.Project;
import main.model.Task;
import main.model.User;
import main.service.GroupService;
import main.service.ProjectService;
import main.service.TaskService;

@Controller
public class TaskController {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private  TaskService taskService;
	@Autowired
	private  GroupService groupService;
	@Autowired
	private ProjectService projectService;

	private void loadDropDownLists(List<User>users,
			Model model) {
		
		List<Project>projects=
			new ArrayList<>(projectService.getUserProjects());
		
		ArrayList<UserDto> userDtos = 
		modelMapper.map(users, 
		new TypeToken<ArrayList<UserDto>>() {}.getType());
		ArrayList<ProjectDto> projectDtos = 
		modelMapper.map(projects, 
		new TypeToken<ArrayList<ProjectDto>>() {}.getType());
		
		model.addAttribute("users", userDtos);
		model.addAttribute("projects", projectDtos);
		
	}
	private void loadFormLists(Model model) {
		List<User>users=
		new ArrayList<>
		(groupService.getMembersOfFirstGroup());
		List<Group>groups
		=new ArrayList<>(groupService.getUserAdminGroups());
		ArrayList<GroupDto> groupDtos = 
				modelMapper.map(groups, 
				new TypeToken<ArrayList<GroupDto>>() {}.getType());
		model.addAttribute("groups", groupDtos);
		loadDropDownLists(users, model);
	}
	private void loadFilterLists(Model model) {
		List<User>users=
		new ArrayList<>
		(groupService.getUniqueUsers());
		loadDropDownLists(users, model);
	}
	@ResponseBody
	@GetMapping("/addtask1")
	public  List<UserDto> changeUsers(@RequestParam(value = "groupId")
	Long groupId,Model model) {
		Group group=groupService.getGroup(groupId);
		List<User>users=groupService.getMembers(group);
		ArrayList<UserDto> userDtos = 
				modelMapper.map(users, 
				new TypeToken<ArrayList<UserDto>>() {}.getType());
		return userDtos;
	}
	@GetMapping("/addtask")
	public String viewTaskForm(Model model) {
		model.addAttribute("path", "/addtask");
		model.addAttribute("task", new TaskDto());
		model.addAttribute("header", "Добавить задание");
		loadFormLists(model);
		return "task-form";
	}

	@PostMapping("/addtask")
	public String sendTaskForm(
			@ModelAttribute("task") 
			@Valid TaskDto taskDto,
			BindingResult result,
			Model model
			) {
		
		if (result.hasErrors()) {
			model.addAttribute("path", "/addtask");
			model.addAttribute("header", "Добавить задание");
			loadFormLists(model);
			return "task-form";
        }
		Task task = modelMapper.map(taskDto, Task.class);
		taskService.addTask(task);
		
		return "redirect:/tasks";
	}

	@GetMapping("/tasks")
	public String viewTaskList(Model model) {
		List<Task>tasks=taskService.getUserTasks();
		ArrayList<TaskDto> taskDtos = 
		modelMapper.map(tasks, 
		new TypeToken<ArrayList<TaskDto>>() {}.getType());
		model.addAttribute("tasks", taskDtos);
		model.addAttribute("fo", new FilterObject());
		loadFilterLists(model);
		return "task-list";
	}

	@RequestMapping(value = "/tasks", params = "search", 
			method = RequestMethod.POST)
	String filterTasks(@ModelAttribute("fo") 
	FilterObject fo,
		Model model) {
		
		Filter filter = new Filter();
		if (fo.getExecutor().getId() != null) {
			System.out.println("NOT NULL");
			User user = modelMapper.map(fo.getExecutor(), User.class);
			filter.addCondition(new Condition.Builder().setField("executor")
					.setValue(user).build());
		}
		if (fo.getProject().getId() != null) {
			Project project = modelMapper.map(fo.getProject(), Project.class);
			filter.addCondition(new Condition.Builder().setField("project")
					.setValue(project).build());
		}
		if (fo.getDueDate() != null) {
			filter.addCondition(new Condition.Builder().setField("dueDate")
					.setValue(fo.getDueDate()).build());
		}
		List<Task> tasks = new ArrayList<>();
		if (!filter.isEmpty())
			tasks = taskService.filter(filter);
		if (fo.getSortByDate() != null) {
			Collections.sort(
				tasks, (t1, t2) -> {return t1.getDueDate().
					compareTo(t2.getDueDate());}
			);
		}
		if (fo.getKeyword()!="") {
			ArrayList<Task> keywordTasks=new ArrayList<>();
			for (Task t: tasks) {
				String data = t.getName()+
					t.getDescription()+t.getComments().toString()+ t.getExecutor().getName(); 
				if (data.indexOf(fo.getKeyword())!=-1) 
					keywordTasks.add(t);
			}
			tasks=keywordTasks;
		}
		ArrayList<TaskDto> taskDtos = 
				modelMapper.map(tasks, 
				new TypeToken<ArrayList<TaskDto>>() {}.getType());
		model.addAttribute("tasks", taskDtos);
		loadFilterLists(model);
		return "task-list";
	}
	
	@RequestMapping(value = "/tasks/{id}", 
			params = "fav", method = RequestMethod.POST)
	public String changeFavoriteStatus(@PathVariable Long id, 
			Model model) {
		Task task=taskService.getTask(id);
		if (taskService.isFavorite(task))
			taskService.deleteFromFavorite(task);
		else 
			taskService.addToFavorite(task);
		
		return "redirect:/tasks/"+id;
	}
	@RequestMapping(value = "/tasks/{id}", 
			params = "deleteTask", method = RequestMethod.POST)
	public String deleteTask(@PathVariable Long id, Model model) {
		Task task=taskService.getTask(id);
		taskService.deleteTask(task);
		return "redirect:/tasks";
	}
	@GetMapping("/tasks/{id}")
	public String viewTask(@PathVariable Long id, Model model) {
		Task task = taskService.getTask(id);
		TaskDto taskDto = modelMapper.map(task, TaskDto.class);
		List<Comment>comments = task.getComments();

		ArrayList<CommentDto> commentDtos = 
				modelMapper.map(comments, 
				new TypeToken<ArrayList<CommentDto>>() {}.getType());
		taskDto.getComments().addAll(commentDtos);
		model.addAttribute("task", taskDto);
		model.addAttribute("comment", new CommentDto());
		String message;
		if (taskService.isFavorite(task))
			message = "Удалить из избранного";
		else
			message = "Добавить в избранное";
		model.addAttribute("message", message);
		return "task-view";
	}
	@RequestMapping(value = "/tasks/{id}", 
			params = "edit", method = RequestMethod.POST)
	public String editTask(@PathVariable Long id, Model model) {
		return "redirect:/tasks/"+id+"/edit";
	}
	@PostMapping("/tasks/{id}/edit")
	public String sendEditTaskForm(@PathVariable Long id,
			@ModelAttribute("task") TaskDto taskDto, Model model) {
		Task task = modelMapper.map(taskDto, Task.class);
		taskService.updateTask(task);
		return "redirect:/tasks/"+id;
	}

	@GetMapping("/tasks/{id}/edit")
	public String viewEditTaskForm(@PathVariable Long id, 
			Model model) {
		Task task = taskService.getTask(id);
		TaskDto taskDto = modelMapper.map(task, TaskDto.class);
		model.addAttribute("path", "/tasks/"+id+"/edit");
		model.addAttribute("task", taskDto);
		model.addAttribute("header", "Редактировать задание");
		loadFormLists(model);
		return "task-form";
	}

	@RequestMapping(value = "/tasks/{id}", 
		params = "sendComment", method = RequestMethod.POST)
	public String sendComment(@PathVariable Long id, 
			Model model, 
			@ModelAttribute("comment") CommentDto commentDto) {
		Task task = taskService.getTask(id);
		Comment comment = modelMapper.map(commentDto, 
			Comment.class);
		taskService.addComment(task, comment);
		return "redirect:/tasks/"+id;
	}
	@GetMapping(value = "/favorites")
		public String viewFavorites(Model model) {
			List<Task> favorites = 
					taskService.getUserFavorites();
			model.addAttribute("favorites", favorites);
			return "favorites";
		}
}
