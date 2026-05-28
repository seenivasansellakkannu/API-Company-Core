package com.company.core.process.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Employee {
	
	@JsonProperty("employeeId")
	public Integer employeeId;

	@JsonProperty("employeeName")
	public String employeeName;
	
	@JsonProperty("employeeLevel")
	public String employeeLevel;
	
	@JsonProperty("addressId")
	public String addressId;
	
	@JsonProperty("primaryPhoneNumber")
	public String primaryPhoneNumber;
	
	@JsonProperty("secondaryPhoneNumber")
	public String secondaryPhoneNumber;
	
	@JsonProperty("dateOfJoining")
	public Integer dateOfJoining;
	
	@JsonProperty("dateOfResign")
	public Integer dateOfResign;
	
	@JsonProperty("employeeStatus")
	public String employeeStatus;
	
	@JsonProperty("email")
	public String email;
	
}
