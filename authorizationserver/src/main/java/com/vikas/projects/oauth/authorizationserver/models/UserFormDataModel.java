package com.vikas.projects.oauth.authorizationserver.models;

public class UserFormDataModel {
	
	private String reqId;
	private String[] scopeList;
	private String approve;
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String[] getScopeList() {
		return scopeList;
	}
	public void setScopeList(String[] scopeList) {
		this.scopeList = scopeList;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	
	

}
