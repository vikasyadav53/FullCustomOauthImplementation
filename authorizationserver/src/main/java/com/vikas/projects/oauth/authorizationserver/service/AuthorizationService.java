package com.vikas.projects.oauth.authorizationserver.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vikas.projects.oauth.authorizationserver.models.RequestDetails;
import com.vikas.projects.oauth.authorizationserver.models.UserLoginModel;
import com.vikas.projects.oauth.authorizationserver.repositories.JdbcRepository;

@Service
public class AuthorizationService {

	@Autowired
	private JdbcRepository jdbcRepository;

	public String validateUser(UserLoginModel userLogin) {
		String status = jdbcRepository.findUserByUserId(userLogin);
		return status;
	}
	
	public int savedRequestDetails(RequestDetails reqdetails) {
		int hashValue = generateHasCoderequestDetails(reqdetails);
		reqdetails.setHashValue(hashValue);
		jdbcRepository.saveRequestDetailsByhash(reqdetails, hashValue);
		return hashValue;
	}

	private int generateHasCoderequestDetails(RequestDetails reqdetails) {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(reqdetails.getApprovedScopes());
		result = prime * result + Arrays.hashCode(reqdetails.getAssignedScopes());
		result = prime * result + ((reqdetails.getClientId() == null) ? 0 : reqdetails.getClientId().hashCode());
		result = prime * result + ((reqdetails.getClientSecret() == null) ? 0 : reqdetails.getClientSecret().hashCode());
		result = prime * result + ((reqdetails.getCode() == null) ? 0 : reqdetails.getCode().hashCode());
		result = prime * result + ((reqdetails.getGrantType() == null) ? 0 : reqdetails.getGrantType().hashCode());
		result = prime * result + Arrays.hashCode(reqdetails.getRedirectUris());
		result = prime * result + ((reqdetails.getReqid() == null) ? 0 : reqdetails.getReqid().hashCode());
		result = prime * result + ((reqdetails.getState() == null) ? 0 : reqdetails.getState().hashCode());
		result = prime * result + ((reqdetails.getUserId() == null) ? 0 : reqdetails.getUserId().hashCode());
		return result;
	}
}
