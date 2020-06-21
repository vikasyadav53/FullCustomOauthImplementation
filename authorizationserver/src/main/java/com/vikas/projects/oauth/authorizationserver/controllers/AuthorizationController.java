package com.vikas.projects.oauth.authorizationserver.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.vikas.projects.oauth.authorizationserver.models.RequestDetails;
import com.vikas.projects.oauth.authorizationserver.models.UserFormDataModel;
import com.vikas.projects.oauth.authorizationserver.models.UserLoginModel;
import com.vikas.projects.oauth.authorizationserver.service.AuthorizationService;

@Controller
public class AuthorizationController {

	@Value("${client_id}")
	private String client_id;
	@Value("${client_secret}")
	private String client_secret;
	@Value("${scope}")
	private String scope;
	@Value("${redirect_uris}")
	private String[] redirect_url;
	@Value("${authorizationEndpoint}")
	private String authorizationEndpoint;
	@Value("${tokenEndpoint}")
	private String tokenEndpoint;

	private String reqid;
	private String responseType;
	private String code;
	private String requestedRedirectUri;
	private String state;
	private String accessToken;
	private String[] rscope;

	@Autowired
	private RequestDetails requestDetails;
	
	@Autowired
	private AuthorizationService service;

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
			@RequestParam("redirect_uri") String redirectUri,
			@RequestParam(name = "scope", required = false) String req_scope,
			@RequestParam("response_type") String responseType, @RequestParam("state") String state) {
		if (!this.client_id.equals(clientId)) {
			model.addAttribute("error", "Unknown client");
			return "error";
		}
		List<String> redirectUrisList = Arrays.asList(redirect_url);
		if (!redirectUrisList.contains(redirectUri)) {
			model.addAttribute("error", "Invalid redirect URI");
			return "error";
		}
		this.requestedRedirectUri = redirectUri;
		rscope = scope != null ? req_scope.split(", ") : null;
		String[] cscope = scope != null ? scope.split(", ") : null;
		if (!validateScopes(rscope, cscope)) {
			Map<String, String> requestParams = new HashMap<>();
			requestParams.put("error", "invalid_scope");

			String builder = requestParams.keySet().stream().map(key -> key + "=" + encodeValue(requestParams.get(key)))
					.collect(Collectors.joining("&", redirectUri + "?", ""));
			return "redirect:" + builder;

		}
		reqid = getRandomString();

		this.responseType = responseType;

		this.state = state;		
		return "login";
	}
	
	@PostMapping("/login")
	public String loginValidation(@ModelAttribute("userlogindetails") UserLoginModel userLogin, Model model) {
		if(userLogin.getUserid() == null || userLogin.getPassword() == null) {
			model.addAttribute("error", "Invalid user details");
			return "error";
		}
		String status  = service.validateUser(userLogin);
		if(status == null ) {
			model.addAttribute("error", "User not found");
			return "error";
		}
		requestDetails.setUserId(status);
		model.addAttribute("client_id", client_id);
		model.addAttribute("client_secret", client_secret);
		model.addAttribute("reqid", reqid);
		model.addAttribute("scope", rscope);
		return "approve";
	}
	

	@PostMapping("/approve")
	public String approveGenerator(@ModelAttribute("user") UserFormDataModel userData, Model model) {
		String retrievedID = userData.getReqId();
		if (retrievedID != reqid) {
			model.addAttribute("error", "No matching authorization request");
			return "error";
		}

		if ("Approve".equalsIgnoreCase(userData.getApprove())) {
			if (this.responseType.equals("code")) {
				code = getRandomString();
				String[] approvedScopes = userData.getScopeList();
				String[] assignedScopes = scope.split(", ");
				if (!validateScopes(approvedScopes, assignedScopes)) {
					Map<String, String> requestParams = new HashMap<>();
					requestParams.put("error", "invalid_scope");
					String builder = requestParams.keySet().stream()
							.map(key -> key + "=" + encodeValue(requestParams.get(key)))
							.collect(Collectors.joining("&", requestedRedirectUri + "?", ""));
					return "redirect:" + builder;
				}
				requestDetails.setClientId(client_id);
				requestDetails.setApprovedScopes(approvedScopes);
				requestDetails.setAssignedScopes(assignedScopes);
				requestDetails.setClientSecret(client_secret);
				requestDetails.setCode(code);
				requestDetails.setGrantType(responseType);
				requestDetails.setRedirectUris(redirect_url);
				requestDetails.setReqid(reqid);
				requestDetails.setState(state);

				Map<String, String> requestParams = new HashMap<>();
				requestParams.put("code", code);
				requestParams.put("state", state);
				String builder = requestParams.keySet().stream()
						.map(key -> key + "=" + encodeValue(requestParams.get(key)))
						.collect(Collectors.joining("&", requestedRedirectUri + "?", ""));
				return "redirect:" + builder;

			} else {
				Map<String, String> requestParams = new HashMap<>();
				requestParams.put("error", "unsupported_response_type");
				String builder = requestParams.keySet().stream()
						.map(key -> key + "=" + encodeValue(requestParams.get(key)))
						.collect(Collectors.joining("&", requestedRedirectUri + "?", ""));
				return "redirect:" + builder;
			}
		} else {
			Map<String, String> requestParams = new HashMap<>();
			requestParams.put("error", "access_denied");
			String builder = requestParams.keySet().stream().map(key -> key + "=" + encodeValue(requestParams.get(key)))
					.collect(Collectors.joining("&", requestedRedirectUri + "?", ""));
			return "redirect:" + builder;
		}
	}

	@PostMapping("/token")
	public ResponseEntity<String> tokenEndpointConfiguration(
			@RequestHeader(name = "authorization", required = true) String authorization,
			@RequestHeader("response_type") String responseType,@RequestHeader("code") String reqCode ) {

		byte[] clientCrdentialsbyte = Base64.getDecoder().decode(authorization.split("Basic, ")[1]);
		String clientCrdentialsString = new String(clientCrdentialsbyte);
		String[] clientCredentials = clientCrdentialsString.split(":");
		String clientId = clientCredentials[0];
		String clientSecret = clientCredentials[1];

		if (requestDetails.getClientId().equals(clientId) || requestDetails.getClientSecret().equals(clientSecret)) {
			return new ResponseEntity<String>("Invalid client", HttpStatus.UNAUTHORIZED);
		}
		
		if (responseType.equals("authorization_code")) {
			String savedCode = requestDetails.getCode();
			if(savedCode.equals(reqCode)) {
				accessToken = getRandomString();
				String approvedScopesList = String.join(", ", requestDetails.getApprovedScopes());
				requestDetails.setAccessToken(accessToken);
				//Save requestDetails in database
				service.savedRequestDetails(requestDetails);
				JSONObject jsonObject= new JSONObject();
				jsonObject.put("access_token", accessToken);
				jsonObject.put("scopes", approvedScopesList);
				
				return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Invalid code received", HttpStatus.UNAUTHORIZED);
			}
		} else {
			return new ResponseEntity<String>("Invalid grant", HttpStatus.UNAUTHORIZED);
		}
	}

	private boolean validateScopes(String[] req_scopes, String[] approved_scopes) {
		List<String> reqScopesList = Arrays.asList(req_scopes);
		List<String> approvedScopesList = Arrays.asList(approved_scopes);
		for (String reqScopes : reqScopesList) {
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

	private String getRandomString() {
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		return new String(array, Charset.forName("UTF-8"));
	}

}
