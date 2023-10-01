package main.service;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import main.model.Group;
import main.model.Member;
import main.model.Role;
import main.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import main.repository.GroupRepository;
import main.repository.MemberRepository;
import main.repository.UserRepository;
import main.auxiliary.Roles;
import main.dto.UserPrincipalDto;


@Transactional
@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ModelMapper modelMapper;
    
    public User getUser(Long id) {
    	return userRepository.findUserById(id);
    }
    public void registerUser(User user)
    {
        if (emailExists(user.getEmail())) {
            throw new RuntimeException("Существует учетная запись "
            		+ "с этим адресом электронной почты: "
              + user.getEmail());
        }
        userRepository.save(user);
        Group group=new Group();
        group.setName("Моя группа");
        groupRepository.save(group);
        Member member = new Member();
        member.setUser(user);
        Role role = new Role();
        role.setRole(Roles.ADMIN);
        member.setRole(role);
        member.setGroup(group);
        memberRepository.save(member);
        loadUserByUsername(user.getEmail());
        
    }
    public void authenticateUser(User user) {
    	if (emailAndPasswordNotExist
    			(user.getEmail(),user.getPassword())) {
    		throw new RuntimeException("You are not registrated yet"
    	              + user.getEmail());
    	}	
    }
    private boolean emailExists(String email) {
        return userRepository.findUserByEmail(email) != null;
    }
    private boolean emailAndPasswordNotExist(String email, String password) {
        return userRepository
        	.findUserByEmailAndPassword(email, password) == null;
    }
    public User getSessionUser() {
    	UserPrincipalDto userPrincipal = 
    		(UserPrincipalDto) SecurityContextHolder
    		.getContext().getAuthentication().getPrincipal();
    	User user = userRepository
    	.findUserByEmail(userPrincipal.getUsername());
    	return user;
    }
    public List<User> getAllUsers() {
    	return userRepository.findAll();
    }

		@Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
			User user = userRepository.findUserByEmail(email);
	        if (user == null) {
	            throw new UsernameNotFoundException(email);
	        }
	
	        UserPrincipalDto userPrincipalDto=modelMapper.map(user, UserPrincipalDto.class);
	        userPrincipalDto.setUsername(email);
	        userPrincipalDto.setPassword(new BCryptPasswordEncoder()
	        	.encode(userPrincipalDto.getPassword()));
	        return userPrincipalDto;
	    }
}