package com.libertymutual.goforcode.localhope.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.libertymutual.goforcode.localhope.models.FollowUniqueCharitiesOnlyException;
import com.libertymutual.goforcode.localhope.models.UniqueEinForCharitiesException;
import com.libertymutual.goforcode.localhope.models.UserD;
import com.libertymutual.goforcode.localhope.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("")
public class SessionController {

	private UserRepository userRepository;
	private PasswordEncoder encoder;
	private SendGridController sendGridController; 

	public SessionController (UserRepository userRepository, PasswordEncoder encoder, SendGridController sendGridController) {
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.sendGridController = sendGridController; 

	}

	@GetMapping("registration")
	public ModelAndView registration() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("");
		return mv;
	}


	@PostMapping("registration")
	public UserD register(@RequestBody UserD user, HttpServletResponse response) throws FollowUniqueCharitiesOnlyException, UniqueEinForCharitiesException, IOException {

		String password = user.getPassword();
		String encryptedPassword = encoder.encode(password);
		user.setPassword(encryptedPassword);
		user.setCharityPreference("");
		user.setDonationPreferences("");
		user.setFollowers("");
		System.out.println("ein" + user.getEin());

		try {
			if (user.getEin() != null && !user.getEin().isEmpty() && userRepository.findByEin(user.getEin()) != null) {
				throw new UniqueEinForCharitiesException();
			}
			userRepository.save(user);
			sendGridController.main(null);
			return user;
		} catch (DataIntegrityViolationException dive) {
			System.out.println("there was an error");
			return null;
		}
	}

	@PostMapping("sessions")
	public UserD login(@RequestBody LoginModel userLogin, HttpServletResponse response) {
		UserD user = userRepository.findByUsername(userLogin.getUsername());
		String password = userLogin.getPassword();

		if (user != null && BCrypt.checkpw(password, user.getPassword())) {
			return user;
		} else {
			return null;
		}
	}

}
