package com.company.core.process.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

//import com.company.core.process.multitenantmanager.MultiTenantManager;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class HealthCheckRepo {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
//	@Autowired
//	MultiTenantManager multiTenantManager;
	
	@Value("${tenantId}")
	private String tenantId;
	
	public String getTestConnection() {
		log.info("getTestConnection method starts");
		try {
//			multiTenantManager.setCurrentTenant(tenantId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return jdbcTemplate.queryForObject("SELECT * FROM test ", String.class);
	}

}
