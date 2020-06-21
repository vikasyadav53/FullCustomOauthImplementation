package com.vikas.projects.oauth.authorizationserver.repositories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.vikas.projects.oauth.authorizationserver.models.RequestDetails;
import com.vikas.projects.oauth.authorizationserver.models.UserLoginModel;
import com.vikas.projects.oauth.authorizationserver.utils.InsertReqestDetailForSession;



public class JdbcRepositoriesImpl implements JdbcRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParamaterJdbcTemplate;

	@Override
	public RequestDetails findDetailByToken() {
		return null;
	}

	@Override
	public String findUserByUserId(UserLoginModel userLogin) {
		String sql = "Select userid from user_table where userid= :userid and password= :userPwd";
		Map<String, String> namedParameters = new HashMap<String, String>();
		namedParameters.put("userid", userLogin.getUserid());
		namedParameters.put("userpwd", userLogin.getPassword());
		return namedParamaterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
	}

	@Override
	public long saveRequestDetailsByhash(RequestDetails reqdetails, int hashValue) {
		InsertReqestDetailForSession insertReqestDetailForSession = new InsertReqestDetailForSession(jdbcTemplate.getDataSource());
		Map<String, Object> namedParams = new HashMap<String, Object>();
		namedParams.put("req_id", reqdetails.getReqid());
		namedParams.put("client_id", reqdetails.getClientId());
		namedParams.put("client_secret", reqdetails.getClientSecret());
		namedParams.put("redirect_uris", Arrays.toString(reqdetails.getRedirectUris()));
		namedParams.put("state", reqdetails.getState());
		namedParams.put("assigned_scopes", Arrays.toString(reqdetails.getAssignedScopes()));
		namedParams.put("approved_scopes", Arrays.toString(reqdetails.getApprovedScopes()));
		namedParams.put("code", reqdetails.getCode());
		namedParams.put("grant_type", reqdetails.getGrantType());
		namedParams.put("user_id", reqdetails.getUserId());
		namedParams.put("creation_time", new java.sql.Date(System.currentTimeMillis()));
		namedParams.put("hash_value", reqdetails.getHashValue());
		namedParams.put("access_token", reqdetails.getAccessToken());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		insertReqestDetailForSession.updateByNamedParam(namedParams, keyHolder);
		long generatedPrimayKey = keyHolder.getKey().longValue();
		return generatedPrimayKey;
		// TODO Auto-generated method stub
		
	}
	
	
}
