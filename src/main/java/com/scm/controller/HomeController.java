package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.dao.UserDao;
import com.scm.entity.User;
import com.scm.helper.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDao userDao;
	
	@GetMapping("/home")
	public String test(Model m) 
	{
		System.out.println("Home Page Started....");
		m.addAttribute("title", "Home - Smarter Contact Manager");
		return "home";
		//http://localhost:8082/user/index
	}
	
	@GetMapping("/about")
	public String about(Model model) 
	{
		System.out.println("About Page Started....");
		model.addAttribute("title", "About - Smarter Contact Manager");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) 
	{
		System.out.println("Signup Page Started....");
		model.addAttribute("title", "Register - Smarter Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user" ) User user,BindingResult result1,@RequestParam(value="agreement",defaultValue="false") boolean agreement,Model model,HttpSession session,HttpServletRequest request)
	{ 
		try {
			if(!agreement)
			{
				System.out.println("DO_REGISTER Page Started....");
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}
			
			if(result1.hasErrors())
			{
				System.out.println("ERROR" + result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.jpg");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			System.out.println("Agreement = " + agreement);
			System.out.println("User = " + user);
			
			User result = this.userDao.save(user);
			
			model.addAttribute("user",new User());//user,user-object
			
			session.setAttribute("message", new Message("Successfully Registered !!" , "alert-success"));
			return "signup";
		}
		catch (Exception e) {
			
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong !!" + e.getMessage(),"alert-danger"));
			return "signup";
			
		}
	}


	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		System.out.println("Login Page Started....");
		model.addAttribute("title", "Login - Smarter Contact Manager");
		return "login";
	}
	
	
}
	

	

	
	

	
	
	
	
	
	
	
	
	
	
	
	

	/*
	 * @Autowired private UserDao userDao;
	 * 
	 * @GetMapping("/h")
	 * 
	 * @ResponseBody public String test() { 
	 * User user= new User();
	 * user.setName("XYZ"); 
	 * user.setEmail("a@gmail.com");
	 * 
	 * Contact contact=new Contact(); user.getContacts().add(contact);
	 * 
	 * userDao.save(user); return "home"; }
	 */

