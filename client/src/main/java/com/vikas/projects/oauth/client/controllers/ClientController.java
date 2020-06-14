package com.vikas.projects.oauth.client.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class ClientController {

	private String state = null;
	private String access_token = null;
	private String scope = null;

	private String response_type = "code";
	@Value("${client_id}")
	private String client_id;
	@Value("${redirect_uris}")
	private String[] redirect_uri;
	@Value("${client_secret}")
	private String client_secret;
	@Value("${tokenEndpoint}")
	private String tokenEndpointURL;
	
	private String code;

	@GetMapping("/")
	public String indexRenderer(Model model) {
		model.addAttribute("access_token", access_token);
		model.addAttribute("scope", scope);
		return "index";
	}

	@GetMapping("/authorize")
	public String authorizationEndpointConfiguration(HttpServletResponse response) {

		access_token = null;
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		state = new String(array, Charset.forName("UTF-8"));

		Map<String, String> requestParams = new HashMap<>();
		requestParams.put("response_type", response_type);
		requestParams.put("client_id", client_id);
		requestParams.put("redirect_uri", redirect_uri[0]);
		requestParams.put("state", state);

		String builder = requestParams.keySet().stream().map(key -> key + "=" + encodeValue(requestParams.get(key)))
				.collect(Collectors.joining("&", "http://localhost:9001/authorize?", ""));
		return "redirect:" + builder;
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

	@GetMapping("/callback")
	public String tokenEndPointConfiguration(@RequestParam(name = "error", required = false) String error,
			@RequestParam(name = "state", required = true) String state, @RequestParam(name = "code", required = true) String code, Model model) {
		
		if (error != null || !StringUtils.isEmpty(error)) {
			model.addAttribute("error", error);
			return "error";
		}
		
		if(state != this.state) {
			model.addAttribute("error", "State value did not match");
			return "error";
		}
		
		this.code = code;
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);		
		httpHeaders.set("Authorization", "Basic "+ customEncoder(client_id+":"+client_secret));
		
		JSONObject jsonObject= new JSONObject();
		jsonObject.put("grant_type", "authorization_code");
		jsonObject.put("code", this.code);
		jsonObject.put("redirect_uri", redirect_uri[0]);
		
		HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), httpHeaders);
		ResponseEntity<String> responseEntityString = restTemplate.postForEntity(tokenEndpointURL, request, String.class);
		
		if(responseEntityString.getStatusCodeValue() >=200 && responseEntityString.getStatusCodeValue()<300) {
			JSONObject body = new JSONObject(responseEntityString.getBody());
			access_token = body.getString("access_token");
			
			System.out.println("Printing access token value: "+ access_token);
		}
		
		
		
		return null;
	}
	
	private String customEncoder(String source) {
		return Base64.getEncoder().encodeToString(source.getBytes());
	}
}
