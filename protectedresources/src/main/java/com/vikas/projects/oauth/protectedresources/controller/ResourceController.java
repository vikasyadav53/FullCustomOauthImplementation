package com.vikas.projects.oauth.protectedresources.controller;

import java.util.Base64;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vikas.projects.oauth.protectedresources.model.RequestBdy;

@RestController
public class ResourceController {

	@PostMapping("/resource")
	public String getResource(@RequestHeader("authorization") String req_header, @RequestBody RequestBdy req_body,
			@RequestParam("access_token") String req_query) {
		String accessToken = null;
		try {
			accessToken = (req_header ==null) ? null :new String(Base64.getDecoder().decode(req_header.split("bearer ")[1]));
		}catch(Exception ex) {
			accessToken = null;
		}
		if (accessToken == null) {
			accessToken = req_body.getAccessToken();
		}
		if (accessToken == null) {
			accessToken = req_query;
		}
		
		// fetch token details from db.
		
		
		return null;
	}

}
