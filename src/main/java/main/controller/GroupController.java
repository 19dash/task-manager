package main.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
import org.springframework.web.bind.annotation.RequestParam;

import main.model.Group;
import main.model.Member;
import main.model.User;
import main.service.GroupService;
import main.service.UserService;
import main.dto.GroupDto;
import main.dto.MemberDto;
import main.dto.UserDto;

@Controller
public class GroupController {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserService userService;
	@GetMapping("/addgroup")
	public String viewGroupForm(Model model) {
		model.addAttribute("group", new GroupDto());
		model.addAttribute("path", "/addgroup");
		model.addAttribute("header", "Добавить группу");
		return "group-form";
	}

	@PostMapping("/addgroup")
	public String sendGroupForm(
			@ModelAttribute("group") 
			GroupDto groupDto,
			BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "group-form";
        }
		Group group = modelMapper.map(groupDto, Group.class);
		groupService.addGroup(group);
		
		return "redirect:/groups";
	}
	@GetMapping("/groups")
	public String viewGroupList(Model model) {
		ArrayList<Group> groups = 
			new ArrayList<>(groupService.getUserGroups());
		ArrayList<GroupDto> groupDtos = 
				modelMapper.map(groups, 
				new TypeToken<ArrayList<GroupDto>>() {}.getType());
		
		model.addAttribute("groups", groupDtos);
		return "group-list";
	}

	@GetMapping("/groups/{id}")
	public String viewGroup(@PathVariable Long id, 
			Model model) {
		Group group = groupService.getGroup(id);
		
		GroupDto groupDto = modelMapper.map(group, 
				GroupDto.class);
		ArrayList<MemberDto> members = 
				modelMapper.map(group.getMembers(), 
				new TypeToken<ArrayList<MemberDto>>() {}.getType());
		groupDto.getMembers().addAll(members);
		List<User>users=groupService.getUsersToInvite(group);
		
		ArrayList<UserDto> userDtos = 
				modelMapper.map(users, 
				new TypeToken<ArrayList<UserDto>>() {}.getType());
		if (users.size()!=0)
			model.addAttribute("users", userDtos);
		model.addAttribute("user", new UserDto());
		model.addAttribute("group", groupDto);
		Member m = groupService.getMemberByUserIdAndGroupId(group);
		MemberDto curUser = modelMapper.map(m, 
				MemberDto.class);
		model.addAttribute("curUser", curUser);
		return "group-view";
	}

	@RequestMapping(value = "/groups/{id}", 
			params = "invite", method = RequestMethod.POST)
	public String inviteUser(@PathVariable Long id, 
			@ModelAttribute("user") UserDto userDto, 
			Model model) {
		if (userDto.getId()!=null) {
			Group group = groupService.getGroup(id);
			User user = modelMapper.map(userDto, User.class);
			groupService.inviteUser(user, group);
		}
		return "redirect:/groups/"+id;
	}
	@RequestMapping(value = "/groups/{id}", 
			params = "edit", method = RequestMethod.POST)
	public String editProject(@PathVariable Long id, 
			Model model) {
	
		return "redirect:/groups/"+id+"/edit";
	}
	@RequestMapping(value = "/groups/{id}", 
			params = "deleteMember", method = RequestMethod.POST)
	public String deleteUser(@PathVariable Long id, 
			@RequestParam("deleteMember") Long userId, Model model) {
		
		Group group = groupService.getGroup(id);
		User user = userService.getUser(userId);
		groupService.deleteMember(user, group);
		return "redirect:/groups/"+id;
	}
	@RequestMapping(value = "/groups/{id}", 
			params = "deleteGroup", method = RequestMethod.POST)
	public String deleteGroup(@PathVariable Long id, Model model) {
		Group group = groupService.getGroup(id);
		groupService.deleteGroup(group);
		return "redirect:/groups";
	}
	@GetMapping("/groups/{id}/edit")
	public String viewEditGroupForm(@PathVariable Long id, 
			Model model) {
		Group group = groupService.getGroup(id);
		GroupDto groupDto = 
				modelMapper.map(group, GroupDto.class);
		model.addAttribute("group", groupDto);
		model.addAttribute("path", "/groups/"+id+"/edit");
		model.addAttribute("header", "Редактировать группу");
		return "group-form";
	}
	@PostMapping("/groups/{id}/edit")
	public String sendEditGroupForm(@PathVariable Long id,
			@ModelAttribute("group") GroupDto groupDto,
			Model model) {
		Group group = modelMapper.map(groupDto, 
			Group.class);
		groupService.updateGroup(group);
		return "redirect:/groups/"+id;
	}
}
