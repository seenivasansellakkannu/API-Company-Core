package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EmployeeUpdateBasic {
	
	@JsonProperty("employee")
	public Employee employee;
	
	@JsonProperty("address")
	public Address address;
	
	@JsonProperty("salary")
	public Salary salary;
	
}
