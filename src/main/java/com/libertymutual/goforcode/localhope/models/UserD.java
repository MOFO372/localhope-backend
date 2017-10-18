package com.libertymutual.goforcode.localhope.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.libertymutual.goforcode.localhope.repositories.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class UserD implements UserDetails {

	@Transient
	private UserRepository userRepository;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(length = 200, nullable = false)
	private String firstName;

	@Column(length = 200, nullable = false)
	private String lastName;

	@Column(length = 200, nullable = false)
	private String streetAddress;

	@Column(length = 50, nullable = false)
	private String city;


	@Column(length = 2, nullable = false) // ??
	private String state;

	@Column(length = 10, nullable = false)
	private String zipCode;

	@Column(length = 15, nullable = false)
	private String phone;

	
	@Column(length = 100, nullable = false)
	private String email;

	@Column(name="role_name", nullable = false)
	private String isCharity;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserRole> roles;
	
	
	// User only  -----------------------------------	
	@Column(length=200)
	private String donationPreferences;      
	
	@Column(length=100)						
	private String charityPreference;	
	
	// Followed Charities   
	@Column
	private String followedCharities= "";
	
	// Followers
	@Column
	private String followers;
	
	// Charity only  -----------------------------------
	@Column(length=200)					
	private String charityName = "NA"; // VALIDATION?  TODO if(ein != null && !ein.isEmpty()) --> charityName has to be populated ??
	
	// IRS: "EIN is a unique 9-digit number", e.g. 01-0553690
	@Column(length = 10)
	private String ein;

	@Column(length = 20)
	private String charityUserRole = "NA";

	@Column(length = 20)
	private String charityType = "NA"; // Charity type/category?? {health, education, puppies... }

	@JsonIgnore
	@ManyToMany(mappedBy = "users")
	private List<Need> needs;

	public UserD() {
	}

	// List<Need> needs
	public UserD(Long id, String username, String password, String isCharity, String firstName,
			String lastName, String streetAddress, String city, String state, String zipCode, String phone,
			String email, String donationPreferences, String charityPreference, String followedCharities, String followers, 
			String charityName, String ein, String charityUserRole, String charityType) {

		this.id = id;
		this.username = username;
		this.password = password;
		this.isCharity = isCharity;
		this.firstName = firstName;
		this.lastName = lastName;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.phone = phone;
		this.email = email;
		// this.role = role;

		this.donationPreferences = donationPreferences;
		this.charityPreference = charityPreference;
		this.followedCharities = followedCharities;
		this.followers = followers;

		this.charityName = charityName;
		this.ein = ein;
		this.charityUserRole = charityUserRole;
		this.charityType = charityType;

	}

	
	// Associate a Need with a User (either DoGooder or Charity)
	public void addNeed(Need need) {
		if (needs == null) {
			needs = new ArrayList<Need>();
		}

		needs.add(need);			
		need.getUsers().add(this);
	}

	
	// Add a Charity to the list of followed charities
	public void addFollowedCharity(UserD charity) throws ThisIsNotACharityException, FollowUniqueCharitiesOnlyException {
		
		if (!charity.getIsCharity().equals("Charity")) {
			throw new ThisIsNotACharityException();
		}
		if (followedCharities.indexOf(charity.getEin()) > -1) {
			throw new FollowUniqueCharitiesOnlyException();
		}
		followedCharities +=  " " + charity.getEin();
		followedCharities.trim();
	}

	
	// Add a DoGooder to the list of a charity's followers
	public void addFollowers(UserD user) throws ThisIsNotAUserException {
		if (!user.getIsCharity().equals("User")) {
			throw new ThisIsNotAUserException();
		}		
		followers += " " + user.getUsername();
		followers.trim();
	}
	
	
	// Remove a Charity from the list of followed charities
	public void removeFollowedCharity(UserD charity)
			throws ThisIsNotACharityException, UnableToDeFollowThisCharityException {
		
		if (!charity.getIsCharity().equals("Charity")) {
			throw new ThisIsNotACharityException();
		}
		if (followedCharities.indexOf(charity.getEin()) < 0) {
			throw new UnableToDeFollowThisCharityException();
		}
		
		String temp = charity.getEin(); // redo
		followedCharities = followedCharities.replace(temp.trim(), ""); // redo
		followedCharities.trim();
	}

	
	// Returns an ArrayList populated with Users who have followed this charity 
	public ArrayList<UserD> listFollowers(UserRepository userRepository)  {				
		String[] userNames = followers.trim().split("\\s+");
		
		ArrayList<UserD> users = new ArrayList<UserD>(); 
		
		for(int i = 0; i < userNames.length; i++) {
							System.out.println(userRepository.findByUsername(userNames[i]));  // delete
			users.add(userRepository.findByUsername(userNames[i]));
		}
		return users;
	}
	
	
	// Returns an ArrayList populated with EINs from the followedCharities Strings 
	public ArrayList<UserD> listFollowedCharities(UserRepository userRepository)  {		
		String[] charityNames = followedCharities.trim().split("\\s+");		
		ArrayList<UserD> charities = new ArrayList<UserD>(); 
		
		// For DoGooder who is not following any Charities return [] 
		for(int i = 0; i < charityNames.length; i++) {
			if (charityNames[i].length() > 0) {
				charities.add(userRepository.findByEin(charityNames[i]));	
			}
		}
		return charities;
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

	public String getIsCharity() {
		return isCharity;
	}

	public void setIsCharity(String isCharity) {
		this.isCharity = isCharity;
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

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>(); 
		roles.add(new SimpleGrantedAuthority("ROLE_" + isCharity));

		return roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public List<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}

	public String getFollowers() {
		return followers;
	}

	public void setFollowers(String followers) {
		this.followers = followers;
	}

}
