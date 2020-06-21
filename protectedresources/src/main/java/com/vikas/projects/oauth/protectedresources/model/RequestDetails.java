package com.vikas.projects.oauth.protectedresources.model;

import java.util.Arrays;

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
	private String userId;
	private int hashValue;
	private String accessToken;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
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
	
	public int getHashValue() {
		return hashValue;
	}
	public void setHashValue(int hashValue) {
		this.hashValue = hashValue;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	@Override
	public String toString() {
		return "RequestDetails [reqid=" + reqid + ", clientId=" + clientId + ", clientSecret=" + clientSecret
				+ ", redirectUris=" + Arrays.toString(redirectUris) + ", state=" + state + ", assignedScopes="
				+ Arrays.toString(assignedScopes) + ", approvedScopes=" + Arrays.toString(approvedScopes) + ", code="
				+ code + ", grantType=" + grantType + ", userId=" + userId + "]";
	}
	
	
	
	

}
