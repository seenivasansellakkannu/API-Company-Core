package com.company.core.process.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Project {

	@JsonProperty("projectId")
	public Integer projectId;
	
	@JsonProperty("projectCode")
	public String projectCode;
	
	@JsonProperty("projectName")
	public String projectName;
	
	@JsonProperty("clientName")
	public String clientName;
	
	@JsonProperty("totalSquareFeet")
	public BigDecimal totalSquareFeet = BigDecimal.ZERO;
	
	@JsonProperty("squareFeetRate")
	public BigDecimal squareFeetRate = BigDecimal.ZERO;
	
	@JsonProperty("estimatedAmount")
	public BigDecimal estimatedAmount  = BigDecimal.ZERO;
	
	@JsonProperty("addressId")
	public Integer addressId;
	
}
