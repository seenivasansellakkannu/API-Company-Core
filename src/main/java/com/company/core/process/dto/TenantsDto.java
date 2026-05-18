package com.company.core.process.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantsDto {
	
	String tenantId;
	
	String url;
	
	String username;
	
	String password;
	
	String driverClass;
	
	String clientCode;
	
	int maximumPoolSize;
	
	int minimumIdle;
}
