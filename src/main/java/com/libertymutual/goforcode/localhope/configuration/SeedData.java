package com.libertymutual.goforcode.localhope.configuration;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.libertymutual.goforcode.localhope.models.Need;
import com.libertymutual.goforcode.localhope.models.UserD;
import com.libertymutual.goforcode.localhope.repositories.NeedRepository;
import com.libertymutual.goforcode.localhope.repositories.UserRepository;




@Configuration
@Profile("development")
public class SeedData {
	
	// , PasswordEncoder encoder
	public SeedData(UserRepository userRepository, NeedRepository needRepository) {
	
        
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        
		needRepository.save(new Need(1L, "crib",  false, "We need those cribs!", 10, sqlDate));
		needRepository.save(new Need(2L, "money", false, "We need to buy more cribs.", 200, sqlDate));
		needRepository.save(new Need(3L, "volunteer", false, "We need to deliver them cribs.", 6, sqlDate));
		
//		Long id, String firstName, String lastName, String streetAddress, String city, String state,
//		String zipCode, String phone, String email, String role, String donationPreferences,
//		String charityPreference, String followedCharities, String charityName,
		
		userRepository.save(new UserD(11L, "John", "Dee", "123 Main", "Seattle", "WA", "98199",
				"(206) 444-4444", "find.me@if.you.can", "DoGooder", "money", "health", "", "", "", "", ""));
		
		userRepository.save(new UserD(11L, "Two", "Way", "Street", "Seattle", "WA", "98199",
				"(206) 444-4444", "find.me@if.you.can", "DoGooder", "money", "health", "01-1234111 01-1234222 01-1234333", "", "", "", ""));
		
		
		
		userRepository.save(new UserD(12L, "Peter", "Allison", "321 Pine", "Seattle", "WA", "98195",
	            "(206) 333-4444", "find.me@if.you.can", "Charity", "", "", "", "ABC H", "01-1234111", "Assistant", "health"));	
		
		userRepository.save(new UserD(12L, "Peter", "Markov", "321 Pine", "Seattle", "WA", "98195",
	            "(206) 333-4444", "find.me@if.you.can", "Charity", "", "", "", "Civic-1", "01-1234222", "Assistant", "civic"));	

		userRepository.save(new UserD(12L, "Peter", "Chebyshev", "321 Pine", "Seattle", "WA", "98195",
	            "(206) 333-4444", "find.me@if.you.can", "Charity", "", "", "", "Civic-2", "01-1234333", "Assistant", "civic"));	
	
		userRepository.save(new UserD(12L, "Peter", "Kolmogorov", "321 Pine", "Seattle", "WA", "98195",
	            "(206) 333-4444", "find.me@if.you.can", "Charity", "", "", "", "Educ", "01-123444", "Assistant", "education"));	
	
					
	}
}


















