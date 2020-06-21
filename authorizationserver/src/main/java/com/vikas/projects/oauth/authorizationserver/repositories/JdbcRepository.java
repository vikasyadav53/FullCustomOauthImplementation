package com.vikas.projects.oauth.authorizationserver.repositories;

import org.springframework.stereotype.Repository;

import com.vikas.projects.oauth.authorizationserver.models.RequestDetails;
import com.vikas.projects.oauth.authorizationserver.models.UserLoginModel;

@Repository
public interface JdbcRepository {

	public RequestDetails findDetailByToken();

	public String findUserByUserId(UserLoginModel userLogin);

	public long saveRequestDetailsByhash(RequestDetails reqdetails, int hashValue);

}
