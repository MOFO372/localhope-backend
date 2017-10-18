package com.libertymutual.goforcode.localhope.controllers;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libertymutual.goforcode.localhope.models.Need;
import com.libertymutual.goforcode.localhope.models.UserD;
import com.libertymutual.goforcode.localhope.repositories.NeedRepository;
import com.libertymutual.goforcode.localhope.repositories.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("")
public class MessageController {

	private UserRepository userRepository;
	private NeedRepository needRepository;

	public MessageController(NeedRepository needRepository, UserRepository userRepository) {
		this.needRepository = needRepository;
		this.userRepository = userRepository;
	}

	@PostMapping("message/{charityid}")
	public String sendMessage(@PathVariable long charityid, @RequestBody long needid) {
		String ACCOUNT_SID = "AC30b2203fa2ba1ca8bbec30eb6b90f28b";
		String AUTH_TOKEN = "641d50e26cc03dba2048ec3a8cab7550";
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		Need need = needRepository.findOne(needid);

		UserD charity = userRepository.findOne(charityid);

		String needMessage = "What we need: " + need.getOriginalAmount() + " of " + need.getDescription() + " by "
				+ need.getDateNeeded();

		ArrayList<UserD> followers = charity.listFollowers(userRepository);

		if (followers.get(0) == null) {
			return "You can't send any messages because you don't have any followers.  How sad.  :(";
		}

		for (int i = 0; i < followers.size(); i++) {
			UserD follower = followers.get(i);
			String phone = follower.getPhone();
			Message message = Message.creator(new PhoneNumber(phone), new PhoneNumber("+15018304032"), needMessage)
					.create();
		}

		return "message sent";
	}
}
