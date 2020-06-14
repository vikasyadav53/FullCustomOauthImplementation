package com.vikas.projects.oauth.authorizationserver.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

	@Value("${client_id}")
	private String client_id;
	@Value("${client_secret}")
	private String client_secret;
	@Value("${scope}")
	private String scope;
	@Value("${redirect_url}")
	private String[] redirect_url;
	@Value("${authorizationEndpoint}")
	private String authorizationEndpoint;
	@Value("${tokenEndpoint}")
	private String tokenEndpoint;
	
	private String reqid;

	@GetMapping("/")
	public String defaultRenderer(Model model) {
		model.addAttribute("client_id", client_id);
		model.addAttribute("client_secret", client_secret);
		model.addAttribute("scope", scope);
		model.addAttribute("redirect_uris", redirect_url);
		model.addAttribute("authorizationEndpoint", authorizationEndpoint);
		model.addAttribute("tokenEndpoint", tokenEndpoint);
		return "index";
	}

	@GetMapping("/authorize")
	public String authorizationEndPointManager(@RequestParam("client_id") String clientId, Model model,
			@RequestParam("redirect_uri") String redirectUri, @RequestParam("scope") String req_scope) {
		if (this.client_id != clientId) {
			model.addAttribute("error", "Unknown client");
			return "error";
		}
		List<String> redirectUrisList = Arrays.asList(redirect_url);
		if (!redirectUrisList.contains(redirectUri)) {
			model.addAttribute("error", "Invalid redirect URI");
			return "error";
		}
		String[] rscope = scope != null ? req_scope.split(", ") : null;
		String[] cscope = scope != null ? scope.split(", ") : null;
		if(!validateScopes(rscope, cscope)) {
			Map<String, String> requestParams = new HashMap<>();
			requestParams.put("error", "invalid_scope");

			String builder = requestParams.keySet().stream().map(key -> key + "=" + encodeValue(requestParams.get(key)))
					.collect(Collectors.joining("&", redirectUri+"?", ""));
			return "redirect:" + builder;
			
		}
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		reqid = new String(array, Charset.forName("UTF-8"));
		
		model.addAttribute("client_id", client_id);
		model.addAttribute("client_secret", client_secret);
		model.addAttribute("reqid", reqid);
		model.addAttribute("scope", rscope);
		return "approve";
	}
	
	@PostMapping("/approve")
	public String approveGenerator(@RequestBody() String body) {
		return null;
	}
	
	
	private boolean validateScopes(String[] req_scopes, String[] approved_scopes) {
		List<String> reqScopesList = Arrays.asList(req_scopes);
		List<String> approvedScopesList = Arrays.asList(approved_scopes);
		for (String reqScopes: reqScopesList) {
			if (!approvedScopesList.contains(reqScopes)) {
				return false;
			}
		}
		return true;
	}
	
	private String encodeValue(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
