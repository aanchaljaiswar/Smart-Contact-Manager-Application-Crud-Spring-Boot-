package com.scm.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="CONTACT")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid ;
	
	@NotBlank(message="Name field is required..!!")
	@Size(min= 3,max = 20 ,message="Name must be between 3 to 20 Chararcters !!")
	private String name;
	
	@NotBlank(message="Nick Name field is required..!!")
	@Size(min= 3,max = 20 ,message="Name must be between 3 to 20 Chararcters !!")
	private String secondName;
	
	@NotBlank(message="Write Your Professional Work...Don't Leave Blank..!!")
	private String work;
	
	
	@Column(unique = true)
	@Pattern(regexp="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message="Please enter a valid email address (e.g., example@example.com)")
	private String email;
	
	@NotNull(message = "Phone number is required...!!")
	@Pattern(regexp="^\\+?[0-9\\s()-]{7,20}$",message = "Invalid phone number format. Please enter a valid phone number.")
	private String phone;
	
	@NotEmpty(message = "Image field cannot be empty...!!")
    @Pattern(regexp = "^(?i).+\\.(jpg|jpeg|png|gif)$", message = "Invalid image file format. Only JPG, JPEG, PNG, and GIF files are allowed.")
	private String image;
	
	@Column(length = 1000)
	@NotBlank(message="Write Contact Description...!!")
	@Pattern(regexp="^[a-zA-Z0-9\\s.,!?'\"(){}\\[\\]:;@#$%^&*+-_=\\\\/]*$",message="Invalid input. Only numbers, characters, and symbols are allowed.")
	private String description;
	
	@ManyToOne
	private User user;

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

//	@Override
//	public String toString() {
//		return "Contact [cid=" + cid + ", name=" + name + ", secondName=" + secondName + ", work=" + work + ", email="
//				+ email + ", phone=" + phone + ", image=" + image + ", description=" + description + ", user=" + user
//				+ "]";
//	}
//	
	
	
	
}
