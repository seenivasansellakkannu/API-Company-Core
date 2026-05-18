package com.company.core.process.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SalaryCalculationEmployeeDate {
	
	@JsonProperty("workDate")
	public Integer workDate; 
	
	@JsonProperty("workdays")
	public BigDecimal workdays;
	
	@JsonProperty("salary")
	public BigDecimal salary;
	
	
}
