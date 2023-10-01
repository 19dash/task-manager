package main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.auxiliary.Roles;
import main.model.Group;
import main.model.Member;
import main.model.User;
import main.repository.GroupRepository;
import main.repository.MemberRepository;
import main.repository.RoleRepository;
import main.repository.UserRepository;

@Service
public class GroupService {
	@Autowired
	private UserService userService;
	@Autowired
	private  MemberRepository memberRepository;
	@Autowired
	private  GroupRepository groupRepository;
	@Autowired
	private  RoleRepository roleRepository;
	@Autowired
	private  UserRepository userRepository;
	
	public void addGroup(Group group) {
		updateGroup(group);
		Member member = new Member();
		member.setUser(userService.getSessionUser());
		member.setRole(roleRepository.findRoleByRole(Roles.MEMBER));
		member.setGroup(group);
		memberRepository.save(member);
	}
	public void updateGroup(Group group) {
		groupRepository.save(group);
	}
	public void inviteUser(User user, Group group) {
		Member member = new Member();
		member.setUser(user);
		member.setGroup(group);
		member.setRole(roleRepository.findRoleByRole(Roles.MEMBER));
		memberRepository.save(member);
	}
	public void deleteMember(User user, Group group) {
		Member member = 
				memberRepository
				.findMemberByUserIdAndGroupId
				(user.getId(),group.getId());
		memberRepository.delete(member);
	}
	public void deleteGroup(Group group) {
		groupRepository.delete(group);
	}
	public Group getGroup(Long id) {
		return groupRepository.findGroupById(id);
	}
	public List<User> getUsersToInvite(Group group) {
		return userRepository.findUsersOutOfGroup(group.getId());
	}
	public List<Group> getUserGroups() {
		return groupRepository.
			findUserGroups(userService.getSessionUser().getId());
	}
	public List<User> getMembersOfFirstGroup() {
		if (getUserGroups().size()!=0) {
			Group group= getUserGroups().get(0);
			return userRepository.
					findUsersOfGroup(group.getId());
		}
		return new ArrayList<>();
	}
	public List<User> getMembers(Group group) {
		return userRepository.
			findUsersOfGroup(group.getId());
	}
	public List<Group> getUserAdminGroups() {
		return groupRepository
		.findUserGroupsOfRole
		(userService.getSessionUser().getId(),"ADMIN");
	}
	public Member getMemberByUserIdAndGroupId(Group group) {
		System.out.println(memberRepository
				.findMemberByUserIdAndGroupId
				(userService.getSessionUser().getId(), group.getId()));
		return memberRepository
		.findMemberByUserIdAndGroupId
		(userService.getSessionUser().getId(), group.getId());
	}
	public List<User>getUniqueUsers() {
		return userRepository.findUniqueUsers();
	}
}
