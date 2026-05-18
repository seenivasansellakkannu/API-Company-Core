package com.company.core.process.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Salary {

	@JsonProperty("employeeId")
	@JsonIgnore
	public Integer employeeId;
	
	@JsonProperty("employeeLevel")
	@JsonIgnore
	public String employeeLevel;
	
	@JsonProperty("salaryPerHour")
	public BigDecimal salaryPerHour;
	
	
}
