package com.scm.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.scm.dao.ContactDao;
import com.scm.dao.UserDao;
import com.scm.entity.Contact;
import com.scm.entity.User;
import com.scm.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ContactDao contactDao;
	
	//method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model ,Principal principal)
	{
		System.out.println("Normal User Page Started....");
		model.addAttribute("title", "User_Board - Smarter Contact Manager");
		
		String userName = principal.getName();
		System.out.println("USERNAME " + userName);

		//GET THE USER USING  USERNAME(email)
		User user = userDao.getUserByUserName(userName);
		System.out.println("User " + user);
		model.addAttribute("user", user);
	}
	
	//dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		System.out.println("User Dashboard Form Started....");
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}
	
	
	//open add form handler//openAddContactForm()-camel case name
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		System.out.println("Add Contact Form Started....");
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(
			@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Principal principal ,HttpSession session )
	{
		try {
			
		String name=principal.getName();
		User user=this.userDao.getUserByUserName(name);
		
		//processing and uploading file
		if(file.isEmpty())
 		{
			 //if file empty then try our msg
			System.out.println("File Is Empty....");
			contact.setImage("contact.png");
		}
		else
		{
			 //upload the file in folder and update the name to contact
			contact.setImage(file.getOriginalFilename());
			
			File saveFile = new ClassPathResource("static/image").getFile();
			
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
			
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("Image Is Uploaded");
		}
		
		//give user to Contacts n add contact to list 
		user.getContacts().add(contact);
		
		//give contact to user
		contact.setUser(user);
				
		this.userDao.save(user);
		
		System.out.println("DATA of Added Contact " + contact);
		
		System.out.println("Added to DataBase....");
		
		//message success alert on add-contact
		session.setAttribute("message", new Message("Your Contact is Added !! Add More..","success"));
		
		}
		catch(Exception e)
		{
			System.out.println("Error while uploading the file:  " + e.getMessage());
			e.printStackTrace();
			//message error alert on add-contact
			session.setAttribute("message", new Message("Something went wrong !! Try again..!!","danger"));
		}
		return"normal/add_contact_form";
	}
	
	//show contacts handler
	//per page=5[n]
	//current page=0[page]
	@GetMapping("/show-contacts/{page}") 
 	public String showContacts(@PathVariable ("page") Integer page, Model m,Principal principal)
 	{
 		m.addAttribute("title", "Show user Contacts");
 		
 		// List of Contacts
 		String userName = principal.getName();
 		User user = this.userDao.getUserByUserName(userName);
 		
 		 Pageable pageable =PageRequest.of(page, 10);
 		
 		Page<Contact> contacts = this.contactDao.findContactsByUser(user.getId(),pageable);
 		m.addAttribute("contacts", contacts);
 		m.addAttribute("currentPage", page);
 		m.addAttribute("totalPages", contacts.getTotalPages());
 		return "normal/show_contacts";
 	}

	//showing particular contact details
	@GetMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer cid,Model model,Principal principal)
	{
		System.out.println("CID " + cid) ;
		Optional<Contact> contactOptional = this.contactDao.findById(cid);
		Contact contact=contactOptional.get();
		
		//
		String userName=principal.getName();
		User user=this.userDao.getUserByUserName(userName);
		
		if(user.getId()==contact.getUser().getId())
		{
		model.addAttribute("contact",contact);
		model.addAttribute("title", contact.getName());
		}
		return "normal/contact_detail";
	}
	
	//delete contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid,Model model,HttpSession session)
	{
		
		Contact contact=this.contactDao.findById(cid).get();
		
		//check 
		System.out.println("Contact " + contact.getCid());;
		
		contact.setUser(null);
		
		//remove//image//contact.getImage()- delete
		this.contactDao.delete(contact);
		
		System.out.println("DELETED");
		session.setAttribute("message", new Message("Contact Has Been Deleted Successfully...!!","success"));
		
		return "redirect:/user/show-contacts/0";
	} 
	
	//open update form handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid,Model m)
	{
		System.out.println("Update Contact Form Started....");
		m.addAttribute("title", "Update Contact");
		
		Contact contact = this.contactDao.findById(cid).get();
		m.addAttribute("contact", contact);
		
		return "normal/update_form";
	}
	
	//update contact handler
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Principal principal ,HttpSession session)
	{
	try {
//			//old contact details
//			//Contact oldcontactDetail = this.contactDao.findById(contact.getCid()).get();
//			//image
		if(!file.isEmpty())
		{
//				//file work//rewite
//				
//				//delete old pic
////				File deleteFile = new ClassPathResource("static/image").getFile();
////				File file1=new File(deleteFile,oldcontactDetail.getImage());
////				file1.delete();
//				
//				//update new photo
////				File saveFile = new ClassPathResource("static/image").getFile();
////				
////				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
////				
////				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
////				
////				contact.setImage(file.getOriginalFilename());
////				
////				System.out.println("Image Is Uploaded");
			}
////			else
////			{
////				contact.setImage(oldcontactDetail.getImage());
////			}
////			
			User user= this.userDao.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactDao.save(contact);
//			//session.setAttribute("message", new Message ("Your Contact Has Been Updated...!!","success"));
//			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("CONTACT NAME " + contact.getName());
		System.out.println("CONTACT ID " + contact.getCid());
		
		//return "redirect:/user/" + contact.getCid()+"/contact/";
		return"";
	}
}
