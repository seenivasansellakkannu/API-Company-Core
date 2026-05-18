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
public class SalaryCalculationEmployee {
	
	@JsonProperty("employeeId")
	public Integer employeeId;
	
	@JsonProperty("employeeName")
	public String employeeName;
	
	@JsonProperty("salary")
	public List<SalaryCalculationEmployeeDate> salary;
	
	@JsonProperty("totalSalary")
	public BigDecimal totalSalary;
	
	
}
