package com.vikas.projects.oauth.protectedresources.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="custom_oauth_client_details")
public class RequestDetails {	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="req_id")
	private String reqid;	
	@Column(name="client_id")
	private String clientId;	
	@Column(name="client_secret")
	private String clientSecret;	
	@Column(name="redirect_uris")
	private String redirectUris; // comma seperated redirected uris
	@Column(name="state")
	private String state;
	@Column(name="assigned_scopes")
	private String assignedScopes; // comma seperated assigned scopes
	@Column(name="approved_scopes")
	private String approvedScopes; // comma seperated approved scopes 
	@Column(name="code")
	private String code;
	@Column(name="grant_type")
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	public String getRedirectUris() {
		return redirectUris;
	}
	public void setRedirectUris(String redirectUris) {
		this.redirectUris = redirectUris;
	}
	public String getAssignedScopes() {
		return assignedScopes;
	}
	public void setAssignedScopes(String assignedScopes) {
		this.assignedScopes = assignedScopes;
	}
	public String getApprovedScopes() {
		return approvedScopes;
	}
	public void setApprovedScopes(String approvedScopes) {
		this.approvedScopes = approvedScopes;
	}
	@Override
	public String toString() {
		return "RequestDetails [id=" + id + ", reqid=" + reqid + ", clientId=" + clientId + ", clientSecret="
				+ clientSecret + ", redirectUris=" + redirectUris + ", state=" + state + ", assignedScopes="
				+ assignedScopes + ", approvedScopes=" + approvedScopes + ", code=" + code + ", grantType=" + grantType
				+ "]";
	}
}
