package com.vikas.projects.oauth.authorizationserver.utils;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

public class InsertReqestDetailForSession extends SqlUpdate {

	private static final String INSERT_QUERY = "insert into session_table (req_id, client_id, client_secret, redirect_uris, state, assigned_scopes, approved_scopes, code, grant_type, user_id, creation_time, hash_value, access_token) values (:req_id, :client_id, :client_secret, :redirect_uris, :state, :assigned_scopes, :approved_scopes, :code, :grant_type, :user_id, :creation_time, :hash_value, :access_token);";

	@Autowired
	public InsertReqestDetailForSession(DataSource dataSource) {
		super(dataSource, INSERT_QUERY);
		super.declareParameter(new SqlParameter("req_id", Types.VARCHAR));
		super.declareParameter(new SqlParameter("client_id", Types.VARCHAR));
		super.declareParameter(new SqlParameter("client_secret", Types.VARCHAR));
		super.declareParameter(new SqlParameter("redirect_uris", Types.VARCHAR));
		super.declareParameter(new SqlParameter("state", Types.VARCHAR));
		super.declareParameter(new SqlParameter("assigned_scopes", Types.VARCHAR));
		super.declareParameter(new SqlParameter("approved_scopes", Types.VARCHAR));
		super.declareParameter(new SqlParameter("code", Types.VARCHAR));
		super.declareParameter(new SqlParameter("grant_type", Types.VARCHAR));
		super.declareParameter(new SqlParameter("user_id", Types.VARCHAR));
		super.declareParameter(new SqlParameter("access_token", Types.VARCHAR));
		super.declareParameter(new SqlParameter("creation_time", Types.DATE));
		super.declareParameter(new SqlParameter("hash_value", Types.BIGINT));
		super.setGeneratedKeysColumnNames(new String ("id"));
		super.setReturnGeneratedKeys(true);
	}

}
