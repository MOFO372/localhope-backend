package com.libertymutual.goforcode.localhope.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value=HttpStatus.NOT_FOUND, reason = "This Charity is already followed by this DoGooder. Enough is enough...") 
public class FollowUniqueCharitiesOnlyException extends Exception {

		private static final long serialVersionUID = 1L;	

}