package com.vikas.projects.oauth.authorizationserver.models;

import org.springframework.stereotype.Component;

@Component
public class RequestDetails {
	
	private String reqid;
	private String clientId;
	private String clientSecret;
	private String[] redirectUris;
	private String state;
	private String[] assignedScopes;
	private String[] approvedScopes;
	private String code;
	private String grantType;
	public String getReqid() {
		return reqid;
	}
	public void setReqid(String reqid) {
		this.reqid = reqid;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String[] getRedirectUris() {
		return redirectUris;
	}
	public void setRedirectUris(String[] redirectUris) {
		this.redirectUris = redirectUris;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String[] getAssignedScopes() {
		return assignedScopes;
	}
	public void setAssignedScopes(String[] assignedScopes) {
		this.assignedScopes = assignedScopes;
	}
	public String[] getApprovedScopes() {
		return approvedScopes;
	}
	public void setApprovedScopes(String[] approvedScopes) {
		this.approvedScopes = approvedScopes;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	
	

}
