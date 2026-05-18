package com.company.core.process.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SalaryCalculationProject {
	
	@JsonProperty("projectCode")
	public Integer projectCode;
	
	@JsonProperty("projectName")
	public String projectName;
	
	@JsonProperty("employees")
	public List<SalaryCalculationEmployee> employees;
	
	@JsonProperty("totalSalary")
	public BigDecimal totalSalary;
	
	
}
