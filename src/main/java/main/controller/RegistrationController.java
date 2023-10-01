package main.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import main.model.User;
import main.dto.UserPrincipalDto;
import main.service.UserService;

@Controller
public class RegistrationController {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserService userService;

	@GetMapping("/registration")
	public String viewRegistrationForm(Model model) {
	    UserPrincipalDto user = new UserPrincipalDto();
	    model.addAttribute("user", user);
	    return "registration";
	}
	
	@PostMapping("/registration")
	public String registerUserAccount(
	  @ModelAttribute("user") 
	  @Valid UserPrincipalDto userPrincipalDto,
	  BindingResult result,  
	  HttpServletRequest request, Model model) throws ServletException {
		User user = modelMapper.map(userPrincipalDto, 
				User.class);
		user.setEmail(userPrincipalDto.getUsername());
		try {
		userService.registerUser(user);
		}
		catch(RuntimeException e) {
			model.addAttribute("userAlreadyExistError", 
					e.getMessage());
			return "registration";
		}
		request.login(user.getEmail(),user.getPassword());
		return "redirect:/addgroup";
	}

}
