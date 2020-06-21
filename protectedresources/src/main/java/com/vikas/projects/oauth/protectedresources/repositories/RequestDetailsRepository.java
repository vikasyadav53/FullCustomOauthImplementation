package com.vikas.projects.oauth.protectedresources.repositories;

import org.springframework.stereotype.Repository;

import com.vikas.projects.oauth.protectedresources.model.RequestDetails;

@Repository
public interface RequestDetailsRepository{

	public boolean fetchDetsilsByToken(String accessToken);

}
