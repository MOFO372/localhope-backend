package com.libertymutual.goforcode.localhope.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.validator.constraints.Email;
import com.fasterxml.jackson.annotation.JsonIgnore;




@Entity
public class UserD {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
		
	
	
	@Column(length=200, nullable=false)
	private String firstName;

	@Column(length=200, nullable=false)
	private String lastName;
		
	@Column(length=200, nullable=false)
	private String streetAddress;
	
	@Column(length=50, nullable=false)
	private String city;
	
	@Column(length=2, nullable=false)       // ??
	private String state;	
	
	@Column(length=10, nullable=false)
	private String zipCode;
		
	@Column(length=15, nullable=false)
	private String phone;
	
	@Email
	@Column(length=100, nullable=false)
	private String email;
	
	@Column(length=10, nullable=false)
	private String role;
	
	
	// User only  -----------------------------------	
	@Column(length=200)
	private String donationPreferences;      // multiple?   $, volunteer, items ??  ENUMERATION ??
	// private ArrayList<String> donationPreferences; 
	
	@Column(length=100)						// select from charityType values?
	private String charityPreference;	
	
	// Followed Charities   
	private String  followedCharities;
	
	
	// Charity only  -----------------------------------
	@Column(length=200)					
	private String charityName = "NA";         // if(ein != null && !ein.isEmpty())   charityName has to be populated ??
	
	// IRS: "EIN is a unique 9-digit number", e.g. 01-0553690
	@Column(length=10)   // unique?
	private String ein = "00-0000000";
	
	@Column(length=20)
	private String charityUserRole = "NA";

	@Column(length=20)
	private String charityType = "NA";           // Charity type/category?? {health, education, puppies... }


	
	@JsonIgnore  
	@ManyToMany(mappedBy="users")
	private List<Need> needs;
	
	
	public UserD() {}

    // List<Need> needs
	public UserD(Long id, String firstName, String lastName, String streetAddress, String city, String state,
			String zipCode, String phone, String email, String role, String donationPreferences,
			String charityPreference, String followedCharities, String charityName, String ein, String charityUserRole, String charityType) {
		
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.phone = phone;
		this.email = email;
		this.role = role;
		
		this.donationPreferences = donationPreferences;
		this.charityPreference = charityPreference;
		this.followedCharities = followedCharities;
		
		this.charityName = charityName;
		this.ein = ein;
		this.charityUserRole = charityUserRole;
		this.charityType = charityType;
	}


	
	public void addNeed(Need need) { 
		if (needs==null) {
			needs = new ArrayList<Need>();
		}
		needs.add(need);
		need.getUsers().add(this);
	}	
	

	public void addFollowedCharities(UserD charity) throws ThisIsNotACharityException {
		
		if (!charity.getRole().equals("Charity")) {
			throw new ThisIsNotACharityException();		
		}

		this.followedCharities += " " + charity.getCharityName();
	}

	
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}








	public String getCharityName() {
		return charityName;
	}


	public void setCharityName(String charityName) {
		this.charityName = charityName;
	}


	public String getEin() {
		return ein;
	}


	public void setEin(String ein) {
		this.ein = ein;
	}


	public String getCharityUserRole() {
		return charityUserRole;
	}


	public void setCharityUserRole(String charityUserRole) {
		this.charityUserRole = charityUserRole;
	}


	public List<Need> getNeeds() {
		return needs;
	}


	public void setNeeds(List<Need> needs) {
		this.needs = needs;
	}

	public String getCharityType() {
		return charityType;
	}

	public void setCharityType(String charityType) {
		this.charityType = charityType;
	}

	public String getDonationPreferences() {
		return donationPreferences;
	}

	public void setDonationPreferences(String donationPreferences) {
		this.donationPreferences = donationPreferences;
	}

	public String getCharityPreference() {
		return charityPreference;
	}

	public void setCharityPreference(String charityPreference) {
		this.charityPreference = charityPreference;
	}

	public String getFollowedCharities() {
		return followedCharities;
	}

	public void setFollowedCharities(String followedCharities) {
		this.followedCharities = followedCharities;
	}

}
