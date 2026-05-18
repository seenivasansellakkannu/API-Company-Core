package com.company.core.process.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.core.process.repo.HealthCheckRepo;

@Service
public class HealthCheckApiService {

	@Autowired
	private HealthCheckRepo repo ;
	
	public String getHealth() {
		String testValue = repo.getTestConnection();
		return new String(testValue + "successfully");
	}

}